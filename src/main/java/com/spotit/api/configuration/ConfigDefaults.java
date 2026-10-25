package com.spotit.api.configuration;

public final class ConfigDefaults {
    private ConfigDefaults() {
    }

    public static final long JWT_ACCESS_TOKEN_TTL_SECONDS = 3600L;
    public static final long JWT_REFRESH_TOKEN_TTL_SECONDS = 315_360_000L;
    public static final long OTP_TTL_SECONDS = 600L;

    public static final long ADS_DAILY_LIMIT = 5L;
    public static final long CYCLE_DEFAULT_LENGTH = 28L;
    public static final long CYCLE_DEFAULT_PERIOD_LENGTH = 5L;
    public static final long POINTS_DAILY_CLAIM = 5L;
    public static final long POINTS_WATCH_AD = 10L;
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
    public static final String APP_SUPPORT_EMAIL = "spotitinternational@gmail.com";
    public static final String APP_STORE_URL_ANDROID = "https://play.google.com/store/apps/details?id=com.spotit.app";

}
