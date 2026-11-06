package com.spotit.api.configuration;

public final class ConfigDefaults {
    private ConfigDefaults() {
    }

    public static final long JWT_ACCESS_TOKEN_TTL_SECONDS = 3600L;
    public static final long JWT_REFRESH_TOKEN_TTL_SECONDS = 315_360_000L;
    public static final long OTP_TTL_SECONDS = 600L;

    public static final long CYCLE_DEFAULT_LENGTH = 28L;
    public static final long CYCLE_DEFAULT_PERIOD_LENGTH = 5L;
    public static final long POINTS_DAILY_CLAIM = 5L;
    public static final long QUIZ_ROUNDS = 5L;
    public static final long QUIZ_LIVES = 3L;
    public static final long QUIZ_HINT_COST = 5L;
    public static final long SPOTLIGHT_INVITE_EVERY_DAYS = 3L;
    public static final long SPOTLIGHT_INVITE_MAX_DISMISSALS = 3L;
    public static final long QUIZ_TOPICS_PAGE_SIZE = 8L;
    public static final long GIVEDAY_DAY_START = 480L;
    public static final long GIVEDAY_DAY_END = 1080L;
    public static final long GIVEDAY_VISIT_MINUTES = 30L;
    public static final long GIVEDAY_UNITS_PER_KM = 100L;
    public static final long GIVEDAY_GOOD_PICKS = 3L;
    public static final String GIVEDAY_TITLE_THREE = "A perfect give!";
    public static final String GIVEDAY_TITLE_TWO = "Lovely work";
    public static final String GIVEDAY_TITLE_ONE = "A good start";
    public static final String GIVEDAY_TITLE_ZERO = "Something important is missing";
    public static final long PADBANK_FREE_VISITORS = 2L;
    public static final long PADBANK_FREE_DAYS = 2L;
    public static final long PADBANK_HEARTS = 3L;
    public static final long PADBANK_DELIVERY_COST = 2L;
    public static final long PADBANK_VISITOR_FEE = 2L;
    public static final String PADBANK_TITLE_GREAT = "PERFECT!";
    public static final String PADBANK_TITLE_OK = "GOOD!";
    public static final String PADBANK_TITLE_OOPS = "OOPS!";
    public static final String QUIZ_TITLE_WON = "Level won!";
    public static final String QUIZ_TITLE_PERFECT = "Brilliant!";
    public static final String QUIZ_TITLE_LOST = "Out of hearts";
    public static final String PADBANK_DAY_TITLE_PERFECT = "Perfect day!";
    public static final String PADBANK_DAY_TITLE_DONE = "Bank closed for today";
    public static final String PADBANK_DAY_TITLE_LOST = "Out of hearts";
    public static final long SPOTLIGHT_ADMIN_PAGE_SIZE = 50L;
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
