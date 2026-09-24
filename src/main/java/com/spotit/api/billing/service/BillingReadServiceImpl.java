package com.spotit.api.billing.service;

import com.spotit.api.billing.dto.SubscriptionResponse;
import com.spotit.api.billing.entity.SubscriptionStatus;
import com.spotit.api.billing.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingReadServiceImpl implements BillingReadService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponse getStatus(UUID userId) {
        return subscriptionRepository.findByUserId(userId)
                .map(s -> new SubscriptionResponse(s.getStatus() == SubscriptionStatus.active, s.getPlan(), s.getRenewsAt(), s.isAutoRenew()))
                .orElse(new SubscriptionResponse(false, null, null, false));
    }
}
