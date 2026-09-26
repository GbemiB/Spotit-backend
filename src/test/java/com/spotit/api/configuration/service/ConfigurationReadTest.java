package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.configuration.ConfigPropertyCatalog;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

/**
 * The typed getters every other service reads its tunables through (those services mock this one, so
 * the real mapping from the stored rows is pinned here), plus group listing.
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationReadTest {
    @Mock SecurityConfigRepository securityConfigRepository;
    @Mock GlobalConfigRepository globalConfigRepository;
    @Mock EncryptionService encryptionService;

    ConfigurationDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ConfigurationDomainServiceImpl(securityConfigRepository, globalConfigRepository, encryptionService, new ConfigPropertyCatalog());
        lenient().when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(
                SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).otpTtlSeconds(600).build()));
        lenient().when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(GlobalConfig.builder()
                .id(GlobalConfig.SINGLETON_ID)
                .adsDailyLimit(5).cycleDefaultLength(28).cycleDefaultPeriodLength(5).pointsDailyClaim(10).pointsWatchAd(3)
                .accountPurgeGraceDays(30).badgeKnowYourBodyThreshold(10).badgeCycleVeteranThreshold(90)
                .badgeWeekWarriorStreakThreshold(7).cycleHighConfidenceLogThreshold(20)
                .insightIrregularVariationThresholdDays(7).insightUnusualPeriodLengthDeltaDays(2).insightDefaultCycles(6)
                .subscriptionPeriodDays(31).logMaxPeriodRangeDays(14).rewardsHistoryPageSize(20).contentFeedDefaultLimit(12)
                .build()));
    }

    @Test
    void typedGettersReturnTheStoredValues() {
        assertThat(service.getOtpTtlSeconds()).isEqualTo(600);
        assertThat(service.getAdsDailyLimit()).isEqualTo(5);
        assertThat(service.getCycleDefaultLength()).isEqualTo(28);
        assertThat(service.getCycleDefaultPeriodLength()).isEqualTo(5);
        assertThat(service.getPointsDailyClaim()).isEqualTo(10);
        assertThat(service.getPointsWatchAd()).isEqualTo(3);
        assertThat(service.getAccountPurgeGraceDays()).isEqualTo(30);
        assertThat(service.getBadgeKnowYourBodyThreshold()).isEqualTo(10);
        assertThat(service.getBadgeCycleVeteranThreshold()).isEqualTo(90);
        assertThat(service.getBadgeWeekWarriorStreakThreshold()).isEqualTo(7);
        assertThat(service.getCycleHighConfidenceLogThreshold()).isEqualTo(20);
        assertThat(service.getInsightIrregularVariationThresholdDays()).isEqualTo(7);
        assertThat(service.getInsightUnusualPeriodLengthDeltaDays()).isEqualTo(2);
        assertThat(service.getInsightDefaultCycles()).isEqualTo(6);
        assertThat(service.getSubscriptionPeriodDays()).isEqualTo(31);
        assertThat(service.getLogMaxPeriodRangeDays()).isEqualTo(14);
        assertThat(service.getRewardsHistoryPageSize()).isEqualTo(20);
        assertThat(service.getContentFeedDefaultLimit()).isEqualTo(12);
    }

    @Test
    void listByGroupReturnsOnlyThatGroupsProperties() {
        List<GlobalConfigurationResponse> security = service.listByGroup(PropertyNames.GROUP_SECURITY);

        assertThat(security).isNotEmpty().allSatisfy(p -> assertThat(p.groupName()).isEqualTo(PropertyNames.GROUP_SECURITY));
        assertThat(security).extracting(GlobalConfigurationResponse::name).contains(PropertyNames.OTP_TTL_SECONDS);
    }

    @Test
    void listByAnUnknownGroupIsEmpty() {
        assertThat(service.listByGroup("smtp")).isEmpty();
    }
}
