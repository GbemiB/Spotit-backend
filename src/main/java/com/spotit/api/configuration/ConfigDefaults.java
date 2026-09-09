package com.spotit.api.configuration;

/**
 * Seed values for the typed config tables on a fresh database. {@code ConfigurationDomainServiceImpl}
 * fills any column still left at its zero/null default (whether because the row is brand new or
 * because {@code ConfigStoreMigration} found no legacy row to copy from) with the value here.
 */
public final class ConfigDefaults {
    private ConfigDefaults() {
    }

    // security_config
    public static final long JWT_ACCESS_TOKEN_TTL_SECONDS = 3600L;
    // Long-lived on purpose: Spotit is a utility app, not a banking app. A login is meant to last
    // until the user explicitly signs out. Combined with the sliding-expiry refresh in
    // AuthWriteServiceImpl.refresh(), an actively-used session effectively never lapses.
    public static final long JWT_REFRESH_TOKEN_TTL_SECONDS = 315_360_000L;
    public static final long OTP_TTL_SECONDS = 600L;

    // global_config
    public static final long ADS_DAILY_LIMIT = 5L;
    public static final long CYCLE_DEFAULT_LENGTH = 28L;
    public static final long CYCLE_DEFAULT_PERIOD_LENGTH = 5L;
    public static final long POINTS_DAILY_CLAIM = 50L;
    public static final long POINTS_WATCH_AD = 100L;
    public static final long ACCOUNT_PURGE_GRACE_DAYS = 30L;
    public static final long BADGE_KNOW_YOUR_BODY_THRESHOLD = 10L;
    public static final long BADGE_CYCLE_VETERAN_THRESHOLD = 28L;
    public static final long BADGE_WEEK_WARRIOR_STREAK_THRESHOLD = 7L;
    public static final long CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD = 3L;
    public static final long INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS = 4L;
    public static final long INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS = 3L;
    public static final long INSIGHT_DEFAULT_CYCLES = 6L;
    public static final long SUBSCRIPTION_PERIOD_DAYS = 30L;
    public static final long LOG_MAX_PERIOD_RANGE_DAYS = 14L;
    public static final long REWARDS_HISTORY_PAGE_SIZE = 20L;
    public static final long CONTENT_FEED_DEFAULT_LIMIT = 10L;
}
