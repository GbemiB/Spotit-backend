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
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BillingWriteServiceImplTest {
    @Mock SubscriptionRepository subscriptionRepository;
    @Mock UserRepository userRepository;
    @Mock ConfigurationDomainService configurationDomainService;

    BillingWriteServiceImpl service;
    UUID userId = UUID.randomUUID();
    User user;

    @BeforeEach
    void setUp() {
        service = new BillingWriteServiceImpl(subscriptionRepository, userRepository, configurationDomainService);
        user = User.builder().id(userId).premium(false).build();
    }

    private void stubUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    }

    private Subscription subscription(SubscriptionStatus status, Instant renewsAt) {
        return Subscription.builder().userId(userId).plan("monthly").platform(Platform.ios).receipt("old")
                .status(status).autoRenew(true).renewsAt(renewsAt).build();
    }

    @Test
    void subscribeCreatesAnActiveSubscriptionAndMakesTheUserPremium() {
        when(configurationDomainService.getSubscriptionPeriodDays()).thenReturn(30L);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());
        stubUser();

        SubscriptionResponse response = service.subscribe(userId, new SubscribeRequest("monthly", "android", "receipt"));

        assertThat(response.isPremium()).isTrue();
        assertThat(response.plan()).isEqualTo("monthly");
        Instant expected = Instant.now().plus(Duration.ofDays(30));
        assertThat(response.renewsAt()).isBetween(expected.minusSeconds(60), expected.plusSeconds(60));
        verify(subscriptionRepository).save(any(Subscription.class));
        assertThat(user.isPremium()).isTrue();
    }

    @Test
    void subscribeRejectsABlankReceipt() {
        assertThatThrownBy(() -> service.subscribe(userId, new SubscribeRequest("monthly", "ios", " ")))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.RECEIPT_INVALID);
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void subscribeWhileAlreadyActiveIsRejected() {
        when(subscriptionRepository.findByUserId(userId))
                .thenReturn(Optional.of(subscription(SubscriptionStatus.active, Instant.now().plusSeconds(3600))));

        assertThatThrownBy(() -> service.subscribe(userId, new SubscribeRequest("monthly", "ios", "receipt")))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.ALREADY_SUBSCRIBED);
    }

    @Test
    void subscribeReactivatesAnExpiredSubscription() {
        Subscription expired = subscription(SubscriptionStatus.expired, Instant.now().minusSeconds(3600));
        when(configurationDomainService.getSubscriptionPeriodDays()).thenReturn(30L);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(expired));
        stubUser();

        service.subscribe(userId, new SubscribeRequest("yearly", "android", "new-receipt"));

        assertThat(expired.getStatus()).isEqualTo(SubscriptionStatus.active);
        assertThat(expired.getPlan()).isEqualTo("yearly");
        assertThat(expired.getPlatform()).isEqualTo(Platform.android);
        assertThat(expired.getReceipt()).isEqualTo("new-receipt");
    }

    @Test
    void cancelTurnsOffAutoRenewButKeepsAccessUntilTheRenewalDate() {
        Instant renewsAt = Instant.now().plusSeconds(86_400);
        Subscription sub = subscription(SubscriptionStatus.active, renewsAt);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(sub));

        CancelResponse response = service.cancel(userId);

        assertThat(sub.isAutoRenew()).isFalse();
        assertThat(response.isPremium()).isTrue();
        assertThat(response.accessUntil()).isEqualTo(renewsAt);
    }

    @Test
    void cancelWithoutASubscriptionIs404() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(userId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void restoreKeepsTheCurrentRenewalDateWhenStillEntitled() {
        Instant renewsAt = Instant.now().plusSeconds(86_400);
        Subscription sub = subscription(SubscriptionStatus.active, renewsAt);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(sub));
        stubUser();

        SubscriptionResponse response = service.restore(userId, new RestoreRequest("android", "receipt"));

        assertThat(response.renewsAt()).isEqualTo(renewsAt);
        assertThat(sub.getPlatform()).isEqualTo(Platform.android);
        assertThat(user.isPremium()).isTrue();
    }

    @Test
    void restoreExtendsALapsedSubscription() {
        Subscription sub = subscription(SubscriptionStatus.expired, Instant.now().minusSeconds(86_400));
        when(configurationDomainService.getSubscriptionPeriodDays()).thenReturn(30L);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(sub));
        stubUser();

        service.restore(userId, new RestoreRequest("ios", "receipt"));

        assertThat(sub.getStatus()).isEqualTo(SubscriptionStatus.active);
        assertThat(sub.getRenewsAt()).isAfter(Instant.now().plus(Duration.ofDays(29)));
    }

    @Test
    void restoreWithoutAnyPurchaseIsRejected() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.restore(userId, new RestoreRequest("ios", "receipt")))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NO_PURCHASE_FOUND);
    }

    @Test
    void expireLapsedSubscriptionsRevokesPremium() {
        user.setPremium(true);
        Subscription lapsed = subscription(SubscriptionStatus.active, Instant.now().minusSeconds(60));
        lapsed.setAutoRenew(false);
        when(subscriptionRepository.findByStatusAndAutoRenewFalseAndRenewsAtBefore(any(), any())).thenReturn(List.of(lapsed));
        stubUser();

        service.expireLapsedSubscriptions();

        assertThat(lapsed.getStatus()).isEqualTo(SubscriptionStatus.expired);
        assertThat(user.isPremium()).isFalse();
    }

    @Test
    void settingPremiumForAMissingUserIs404() {
        when(configurationDomainService.getSubscriptionPeriodDays()).thenReturn(30L);
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.subscribe(userId, new SubscribeRequest("monthly", "ios", "receipt")))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }
}
