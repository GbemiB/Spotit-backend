package com.spotit.api.configuration;

/** Names of every row in {@code global_configuration} — the single source of truth so a typo can't silently create a duplicate property. */
public final class PropertyNames {

    private PropertyNames() {
    }

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
    public static final String SUBSCRIPTION_PERIOD_DAYS = "subscription-period-days";
    public static final String LOG_MAX_PERIOD_RANGE_DAYS = "log-max-period-range-days";

    // SMTP: 6 properties per role, not auto-seeded — a role stays "not configured" (see
    // ConfigurationDomainService#getSmtpSettingsInPriorityOrder) until an admin sets its host.
    public static String smtpHost(String role) {
        return "smtp-" + role + "-host";
    }

    public static String smtpPort(String role) {
        return "smtp-" + role + "-port";
    }

    public static String smtpUsername(String role) {
        return "smtp-" + role + "-username";
    }

    public static String smtpPassword(String role) {
        return "smtp-" + role + "-password";
    }

    public static String smtpFromAddress(String role) {
        return "smtp-" + role + "-from-address";
    }

    public static String smtpUseTls(String role) {
        return "smtp-" + role + "-use-tls";
    }
}
