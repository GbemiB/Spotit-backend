package com.spotit.api.configuration.service;

import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.smtp.entity.SmtpRole;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;

import java.util.List;

/**
 * Single source of app-wide configuration — replaces app_settings, smtp_settings, and a handful
 * of hardcoded Java constants (badge thresholds, purge grace period, etc.) with one generic
 * {@code global_configuration} table (see {@link com.spotit.api.configuration.entity.GlobalConfiguration}).
 * Non-SMTP properties self-seed a sensible default the first time they're read, so this never
 * blocks on seeding order at boot (JwtService reads jwt-secret from its own constructor).
 */
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

    // -- SMTP: primary first, then backup, omitting whichever role has no host configured --------

    List<ResolvedSmtpSettings> getSmtpSettingsInPriorityOrder();

    /** Pass {@code password} as {@code null} to leave the previously stored (encrypted) password unchanged. */
    void saveSmtpSettings(SmtpRole role, String host, int port, String username, String password, String fromAddress, boolean useTls);

    // -- generic admin CRUD over every property, SMTP included --------

    List<GlobalConfigurationResponse> listAll();

    GlobalConfigurationResponse getByName(String name);

    GlobalConfigurationResponse update(String name, UpdateGlobalConfigurationRequest request);
}
