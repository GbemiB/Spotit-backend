package com.spotit.api.configuration;

public final class PropertyNames {
    private PropertyNames() {
    }

    public static final String CRYPTO_AES_KEY = "crypto-aes-key";
    public static final String JWT_SECRET = "jwt-secret";
    public static final String JWT_ACCESS_TOKEN_TTL_SECONDS = "jwt-access-token-ttl-seconds";
    public static final String JWT_REFRESH_TOKEN_TTL_SECONDS = "jwt-refresh-token-ttl-seconds";
    public static final String OTP_TTL_SECONDS = "otp-ttl-seconds";
    public static final String ADS_DAILY_LIMIT = "ads-daily-limit";
    public static final String CYCLE_DEFAULT_LENGTH = "cycle-default-length";
    public static final String CYCLE_DEFAULT_PERIOD_LENGTH = "cycle-default-period-length";
    public static final String POINTS_DAILY_CLAIM = "points-daily-claim";
    public static final String POINTS_WATCH_AD = "points-watch-ad";

    public static final String ACCOUNT_PURGE_GRACE_DAYS = "account-purge-grace-days";
    public static final String BADGE_KNOW_YOUR_BODY_THRESHOLD = "badge-know-your-body-threshold";
    public static final String BADGE_CYCLE_VETERAN_THRESHOLD = "badge-cycle-veteran-threshold";
    public static final String BADGE_WEEK_WARRIOR_STREAK_THRESHOLD = "badge-week-warrior-streak-threshold";
    public static final String CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD = "cycle-high-confidence-log-threshold";
    public static final String INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS = "insight-irregular-variation-threshold-days";
    public static final String INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS = "insight-unusual-period-length-delta-days";
    public static final String INSIGHT_DEFAULT_CYCLES = "insight-default-cycles";
    public static final String SUBSCRIPTION_PERIOD_DAYS = "subscription-period-days";
    public static final String LOG_MAX_PERIOD_RANGE_DAYS = "log-max-period-range-days";
    public static final String REWARDS_HISTORY_PAGE_SIZE = "rewards-history-page-size";
    public static final String CONTENT_FEED_DEFAULT_LIMIT = "content-feed-default-limit";

    public static final String GROUP_SECURITY = "security";
    public static final String GROUP_POINTS = "points";
    public static final String GROUP_CYCLE = "cycle";
    public static final String GROUP_BADGES = "badges";
    public static final String GROUP_INSIGHT = "insight";
    public static final String GROUP_BILLING = "billing";
    public static final String GROUP_ACCOUNT = "account";
    public static final String GROUP_LOGS = "logs";
    public static final String GROUP_REWARDS = "rewards";
    public static final String GROUP_CONTENT = "content";
    public static final String GROUP_SMTP = "smtp";
    public static final String GROUP_CHALLENGES = "challenges";
    public static final String GROUP_LEVELS = "levels";

    public static final String SMTP_HOST = "smtp-host";
    public static final String SMTP_PORT = "smtp-port";
    public static final String SMTP_USERNAME = "smtp-username";
    public static final String SMTP_PASSWORD = "smtp-password";
    public static final String SMTP_FROM_ADDRESS = "smtp-from-address";
    public static final String SMTP_USE_TLS = "smtp-use-tls";

    // Badge/challenge definitions are structured records, not scalars — each one is stored as a
    // single row (name-prefixed by id) whose stringValue holds its full JSON encoding, rather
    // than one row per field. See BadgeDefinitionRepository/ChallengeDefinitionRepository.
    public static final String BADGE_DEFINITION_PREFIX = "badge-definition-";
    public static final String CHALLENGE_DEFINITION_PREFIX = "challenge-definition-";
    public static final String LEVEL_DEFINITION_PREFIX = "level-definition-";

    public static String badgeDefinitionName(String id) {
        return BADGE_DEFINITION_PREFIX + id;
    }

    public static String challengeDefinitionName(String id) {
        return CHALLENGE_DEFINITION_PREFIX + id;
    }

    public static String levelDefinitionName(String id) {
        return LEVEL_DEFINITION_PREFIX + id;
    }
}
