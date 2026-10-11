package com.spotit.api.configuration.service;

import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;

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

    List<GlobalConfigurationResponse> listAll();

    List<String> listGroupNames();

    List<GlobalConfigurationResponse> listByGroup(String groupName);

    GlobalConfigurationResponse getByName(String name);

    GlobalConfigurationResponse update(String name, UpdateGlobalConfigurationRequest request);
}
