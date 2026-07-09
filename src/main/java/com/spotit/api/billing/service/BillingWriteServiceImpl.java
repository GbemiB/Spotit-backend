package com.spotit.api.billing.service;

import com.spotit.api.billing.dto.CancelResponse;
import com.spotit.api.billing.dto.RestoreRequest;
import com.spotit.api.billing.dto.SubscribeRequest;
import com.spotit.api.billing.dto.SubscriptionResponse;
import com.spotit.api.billing.entity.Platform;
import com.spotit.api.billing.entity.Subscription;
import com.spotit.api.billing.entity.SubscriptionStatus;
import com.spotit.api.billing.repository.SubscriptionRepository;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingWriteServiceImpl implements BillingWriteService {

    private static final Duration SUBSCRIPTION_PERIOD = Duration.ofDays(30);

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubscriptionResponse subscribe(UUID userId, SubscribeRequest request) {
        if (request.receipt().isBlank()) {
            throw new ApiException(ErrorCode.RECEIPT_INVALID, "Receipt could not be verified.");
        }
        var existing = subscriptionRepository.findByUserId(userId);
        if (existing.isPresent() && existing.get().getStatus() == SubscriptionStatus.active) {
            throw new ApiException(ErrorCode.ALREADY_SUBSCRIBED, "You already have an active subscription.");
        }

        Instant renewsAt = Instant.now().plus(SUBSCRIPTION_PERIOD);
        Subscription sub = existing.orElseGet(() -> Subscription.builder().userId(userId).build());
        sub.setPlan(request.planId());
        sub.setPlatform(Platform.valueOf(request.platform()));
        sub.setReceipt(request.receipt());
        sub.setStatus(SubscriptionStatus.active);
        sub.setAutoRenew(true);
        sub.setRenewsAt(renewsAt);
        subscriptionRepository.save(sub);

        setPremium(userId, true);
        return new SubscriptionResponse(true, sub.getPlan(), sub.getRenewsAt(), true);
    }

    @Override
    @Transactional
    public CancelResponse cancel(UUID userId) {
        Subscription sub = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "No subscription found."));
        sub.setAutoRenew(false);
        subscriptionRepository.save(sub);
        return new CancelResponse(sub.getStatus() == SubscriptionStatus.active, false, sub.getRenewsAt());
    }

    @Override
    @Transactional
    public SubscriptionResponse restore(UUID userId, RestoreRequest request) {
        Subscription sub = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NO_PURCHASE_FOUND, "No previous purchase found for this account."));
        sub.setStatus(SubscriptionStatus.active);
        sub.setAutoRenew(true);
        sub.setPlatform(Platform.valueOf(request.platform()));
        sub.setReceipt(request.receipt());
        sub.setRenewsAt(Instant.now().plus(SUBSCRIPTION_PERIOD));
        subscriptionRepository.save(sub);
        setPremium(userId, true);
        return new SubscriptionResponse(true, sub.getPlan(), sub.getRenewsAt(), true);
    }

    @Override
    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void expireLapsedSubscriptions() {
        var lapsed = subscriptionRepository.findByStatusAndAutoRenewFalseAndRenewsAtBefore(SubscriptionStatus.active, Instant.now());
        for (Subscription sub : lapsed) {
            sub.setStatus(SubscriptionStatus.expired);
            subscriptionRepository.save(sub);
            setPremium(sub.getUserId(), false);
            log.info("Subscription expired for user {}", sub.getUserId());
        }
    }

    private void setPremium(UUID userId, boolean premium) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "User not found."));
        user.setPremium(premium);
        userRepository.save(user);
    }
}
