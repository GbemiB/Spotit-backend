package com.spotit.api.account;

import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.billing.repository.SubscriptionRepository;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.ExportJobRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountPurgeScheduler {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final CycleLogRepository cycleLogRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserChallengeProgressRepository userChallengeProgressRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final DeviceRepository deviceRepository;
    private final ExportJobRepository exportJobRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void purgeDueAccounts() {
        for (User user : userRepository.findByPendingDeletionAtBefore(Instant.now())) {
            purge(user.getId());
            log.info("Purged account {} after deletion grace period", user.getId());
        }
    }

    private void purge(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
        otpCodeRepository.deleteByUserId(userId);
        cycleLogRepository.deleteByUserId(userId);
        pointsHistoryRepository.deleteByUserId(userId);
        userBadgeRepository.deleteByUserId(userId);
        userChallengeProgressRepository.deleteByUserId(userId);
        shopOrderRepository.deleteByUserId(userId);
        subscriptionRepository.deleteByUserId(userId);
        deviceRepository.deleteByUserId(userId);
        exportJobRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
