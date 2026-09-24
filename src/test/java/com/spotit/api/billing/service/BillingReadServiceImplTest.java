package com.spotit.api.billing.service;

import com.spotit.api.billing.dto.SubscriptionResponse;
import com.spotit.api.billing.entity.Subscription;
import com.spotit.api.billing.entity.SubscriptionStatus;
import com.spotit.api.billing.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingReadServiceImplTest {
    @Mock SubscriptionRepository subscriptionRepository;
    @InjectMocks BillingReadServiceImpl service;

    UUID userId = UUID.randomUUID();

    @Test
    void anActiveSubscriptionMeansPremium() {
        Instant renewsAt = Instant.parse("2026-10-24T00:00:00Z");
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(Subscription.builder()
                .userId(userId).plan("monthly").status(SubscriptionStatus.active).autoRenew(true).renewsAt(renewsAt).build()));

        assertThat(service.getStatus(userId)).isEqualTo(new SubscriptionResponse(true, "monthly", renewsAt, true));
    }

    @Test
    void anExpiredSubscriptionIsNotPremium() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(Subscription.builder()
                .userId(userId).plan("monthly").status(SubscriptionStatus.expired).autoRenew(false).build()));

        assertThat(service.getStatus(userId).isPremium()).isFalse();
    }

    @Test
    void noSubscriptionAtAllIsAnEmptyFreeStatus() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThat(service.getStatus(userId)).isEqualTo(new SubscriptionResponse(false, null, null, false));
    }
}
