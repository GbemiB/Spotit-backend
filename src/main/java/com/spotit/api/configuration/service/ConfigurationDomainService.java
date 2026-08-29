package com.spotit.api.configuration.service;

import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.smtp.entity.SmtpRole;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;

import java.util.List;

public interface ConfigurationDomainService {
    String getJwtSecret();

    long getJwtAccessTokenTtlSeconds();

    long getJwtRefreshTokenTtlSeconds();

    long getOtpTtlSeconds();

    int getAdsDailyLimit();

    int getCycleDefaultLength();

    int getCycleDefaultPeriodLength();

    int getPointsDailyClaim();

    int getPointsWatchAd();

    long getAccountPurgeGraceDays();

    int getBadgeKnowYourBodyThreshold();

    int getBadgeCycleVeteranThreshold();

    int getBadgeWeekWarriorStreakThreshold();

    long getCycleHighConfidenceLogThreshold();

    int getInsightIrregularVariationThresholdDays();

    int getInsightUnusualPeriodLengthDeltaDays();

    int getInsightDefaultCycles();

    long getSubscriptionPeriodDays();

    int getLogMaxPeriodRangeDays();

    int getRewardsHistoryPageSize();

    int getContentFeedDefaultLimit();

    List<ResolvedSmtpSettings> getSmtpSettingsInPriorityOrder();

    void saveSmtpSettings(SmtpRole role, String host, int port, String username, String password, String fromAddress, boolean useTls);

    List<GlobalConfigurationResponse> listAll();

    GlobalConfigurationResponse getByName(String name);

    GlobalConfigurationResponse update(String name, UpdateGlobalConfigurationRequest request);
}
