package com.spotit.api.account;

import com.spotit.api.auth.repository.OtpCodeRepository;
import com.spotit.api.auth.repository.RefreshTokenRepository;
import com.spotit.api.billing.repository.SubscriptionRepository;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.partner.repository.PartnerCheckInRepository;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import com.spotit.api.shop.repository.ShopOrderRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.ExportJobRepository;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountPurgeSchedulerTest {
    @Mock UserRepository userRepository;
    @Mock PartnerLinkRepository partnerLinkRepository;
    @Mock PartnerCheckInRepository partnerCheckInRepository;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock OtpCodeRepository otpCodeRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @Mock PointsHistoryRepository pointsHistoryRepository;
    @Mock UserBadgeRepository userBadgeRepository;
    @Mock UserChallengeProgressRepository userChallengeProgressRepository;
    @Mock ShopOrderRepository shopOrderRepository;
    @Mock SubscriptionRepository subscriptionRepository;
    @Mock DeviceRepository deviceRepository;
    @Mock ExportJobRepository exportJobRepository;
    @Mock ReminderDeliveryRepository reminderDeliveryRepository;
    @InjectMocks AccountPurgeScheduler scheduler;

    @Test
    void purgingAnAccountRemovesItsPartnerLinksOnBothSides() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByPendingDeletionAtBefore(any(Instant.class))).thenReturn(List.of(User.builder().id(userId).build()));

        scheduler.purgeDueAccounts();

        verify(partnerLinkRepository).deleteByOwnerId(userId);
        verify(partnerLinkRepository).deleteByPartnerId(userId);
        verify(partnerCheckInRepository).deleteByPartnerId(userId);
        verify(partnerCheckInRepository).deleteByOwnerId(userId);
        verify(userRepository).deleteById(userId);
    }
}
