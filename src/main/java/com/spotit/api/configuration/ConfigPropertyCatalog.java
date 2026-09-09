package com.spotit.api.configuration;

import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.entity.SmtpConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The single source of truth that lets the flat, name-keyed admin API
 * ({@code GET/PATCH /api/v1/config/global/**}) keep working now that settings live in typed
 * columns across {@link SecurityConfig}, {@link SmtpConfig}, and {@link GlobalConfig} instead of
 * one row per property in {@code global_configuration}.
 *
 * <p>Every property the old table exposed is listed here once, together with the metadata the API
 * response needs (group, description, redaction) and a typed getter/setter pair over a
 * {@link Context} holding the three entities. {@code ConfigurationDomainServiceImpl} drives
 * {@code listAll}/{@code getByName}/{@code update}/{@code listByGroup}/{@code listGroupNames}
 * entirely through this catalog, so the three code paths can never drift apart.
 */
@Component
public class ConfigPropertyCatalog {

    public enum Kind { LONG, STRING, BOOL }

    public enum Section { SECURITY, SMTP, GLOBAL }

    /** Mutable holder for the three singleton config rows a getter reads from / a setter writes to. */
    public record Context(SecurityConfig security, SmtpConfig smtp, GlobalConfig global) {
    }

    public static final class Property {
        private final String name;
        private final String group;
        private final String description;
        private final Kind kind;
        private final Section section;
        private final boolean redacted;
        private final boolean encrypted;
        private final Function<Context, Object> getter;
        private final BiConsumer<Context, Object> setter;

        private Property(String name, String group, String description, Kind kind, Section section,
                         boolean redacted, boolean encrypted,
                         Function<Context, Object> getter, BiConsumer<Context, Object> setter) {
            this.name = name;
            this.group = group;
            this.description = description;
            this.kind = kind;
            this.section = section;
            this.redacted = redacted;
            this.encrypted = encrypted;
            this.getter = getter;
            this.setter = setter;
        }

        public String name() {
            return name;
        }

        public String group() {
            return group;
        }

        public String description() {
            return description;
        }

        public Kind kind() {
            return kind;
        }

        public Section section() {
            return section;
        }

        public boolean redacted() {
            return redacted;
        }

        public boolean encrypted() {
            return encrypted;
        }

        public Object read(Context context) {
            return getter.apply(context);
        }

        public void write(Context context, Object value) {
            setter.accept(context, value);
        }
    }

    private final List<Property> ordered;
    private final Map<String, Property> byName;

    public ConfigPropertyCatalog() {
        List<Property> list = new ArrayList<>();

        // --- security_config -------------------------------------------------------------------
        list.add(new Property(PropertyNames.JWT_SECRET, PropertyNames.GROUP_SECURITY,
                "Encrypted JWT signing secret", Kind.STRING, Section.SECURITY, true, true,
                c -> c.security().getJwtSecret(),
                (c, v) -> c.security().setJwtSecret((String) v)));
        list.add(new Property(PropertyNames.JWT_ACCESS_TOKEN_TTL_SECONDS, PropertyNames.GROUP_SECURITY,
                "JWT access token time-to-live, in seconds", Kind.LONG, Section.SECURITY, false, false,
                c -> c.security().getJwtAccessTokenTtlSeconds(),
                (c, v) -> c.security().setJwtAccessTokenTtlSeconds((Long) v)));
        list.add(new Property(PropertyNames.JWT_REFRESH_TOKEN_TTL_SECONDS, PropertyNames.GROUP_SECURITY,
                "JWT refresh token time-to-live, in seconds (10 years — sessions persist until explicit sign-out)",
                Kind.LONG, Section.SECURITY, false, false,
                c -> c.security().getJwtRefreshTokenTtlSeconds(),
                (c, v) -> c.security().setJwtRefreshTokenTtlSeconds((Long) v)));
        list.add(new Property(PropertyNames.OTP_TTL_SECONDS, PropertyNames.GROUP_SECURITY,
                "OTP code time-to-live, in seconds", Kind.LONG, Section.SECURITY, false, false,
                c -> c.security().getOtpTtlSeconds(),
                (c, v) -> c.security().setOtpTtlSeconds((Long) v)));
        list.add(new Property(PropertyNames.CRYPTO_AES_KEY, PropertyNames.GROUP_SECURITY,
                "Root AES-256 key that encrypts every other secret (jwt-secret, smtp-password). Plaintext by "
                        + "necessity — a key can't be encrypted with itself. Never change via the API: rotating it "
                        + "strands every secret already encrypted with the old value.",
                Kind.STRING, Section.SECURITY, true, false,
                c -> c.security().getCryptoAesKey(),
                (c, v) -> c.security().setCryptoAesKey((String) v)));

        // --- smtp_config ---------------------------------------------------------------------
        list.add(new Property(PropertyNames.SMTP_HOST, PropertyNames.GROUP_SMTP,
                "SMTP host used to send all transactional mail", Kind.STRING, Section.SMTP, false, false,
                c -> c.smtp().getHost(),
                (c, v) -> c.smtp().setHost((String) v)));
        list.add(new Property(PropertyNames.SMTP_PORT, PropertyNames.GROUP_SMTP,
                "SMTP port", Kind.LONG, Section.SMTP, false, false,
                c -> (long) c.smtp().getPort(),
                (c, v) -> c.smtp().setPort(((Long) v).intValue())));
        list.add(new Property(PropertyNames.SMTP_USERNAME, PropertyNames.GROUP_SMTP,
                "SMTP username", Kind.STRING, Section.SMTP, false, false,
                c -> c.smtp().getUsername(),
                (c, v) -> c.smtp().setUsername((String) v)));
        list.add(new Property(PropertyNames.SMTP_PASSWORD, PropertyNames.GROUP_SMTP,
                "Encrypted SMTP password", Kind.STRING, Section.SMTP, true, true,
                c -> c.smtp().getPassword(),
                (c, v) -> c.smtp().setPassword((String) v)));
        list.add(new Property(PropertyNames.SMTP_FROM_ADDRESS, PropertyNames.GROUP_SMTP,
                "From address for outgoing mail", Kind.STRING, Section.SMTP, false, false,
                c -> c.smtp().getFromAddress(),
                (c, v) -> c.smtp().setFromAddress((String) v)));
        list.add(new Property(PropertyNames.SMTP_USE_TLS, PropertyNames.GROUP_SMTP,
                "Whether the SMTP relay uses TLS", Kind.BOOL, Section.SMTP, false, false,
                c -> c.smtp().isUseTls(),
                (c, v) -> c.smtp().setUseTls((Boolean) v)));

        // --- global_config ------------------------------------------------------------------
        list.add(new Property(PropertyNames.ADS_DAILY_LIMIT, PropertyNames.GROUP_POINTS,
                "Max rewarded ad views per user per day", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getAdsDailyLimit(),
                (c, v) -> c.global().setAdsDailyLimit((Long) v)));
        list.add(new Property(PropertyNames.POINTS_DAILY_CLAIM, PropertyNames.GROUP_POINTS,
                "Points awarded for the daily claim", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getPointsDailyClaim(),
                (c, v) -> c.global().setPointsDailyClaim((Long) v)));
        list.add(new Property(PropertyNames.POINTS_WATCH_AD, PropertyNames.GROUP_POINTS,
                "Points awarded for watching a rewarded ad", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getPointsWatchAd(),
                (c, v) -> c.global().setPointsWatchAd((Long) v)));
        list.add(new Property(PropertyNames.CYCLE_DEFAULT_LENGTH, PropertyNames.GROUP_CYCLE,
                "Default cycle length assumed until a user has logged enough data", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getCycleDefaultLength(),
                (c, v) -> c.global().setCycleDefaultLength((Long) v)));
        list.add(new Property(PropertyNames.CYCLE_DEFAULT_PERIOD_LENGTH, PropertyNames.GROUP_CYCLE,
                "Default period length assumed until a user has logged enough data", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getCycleDefaultPeriodLength(),
                (c, v) -> c.global().setCycleDefaultPeriodLength((Long) v)));
        list.add(new Property(PropertyNames.CYCLE_HIGH_CONFIDENCE_LOG_THRESHOLD, PropertyNames.GROUP_CYCLE,
                "Logs needed before a cycle's confidence is reported as high", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getCycleHighConfidenceLogThreshold(),
                (c, v) -> c.global().setCycleHighConfidenceLogThreshold((Long) v)));
        list.add(new Property(PropertyNames.ACCOUNT_PURGE_GRACE_DAYS, PropertyNames.GROUP_ACCOUNT,
                "Days after a deletion request before an account is purged", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getAccountPurgeGraceDays(),
                (c, v) -> c.global().setAccountPurgeGraceDays((Long) v)));
        list.add(new Property(PropertyNames.BADGE_KNOW_YOUR_BODY_THRESHOLD, PropertyNames.GROUP_BADGES,
                "Logs required to earn the Know Your Body badge", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getBadgeKnowYourBodyThreshold(),
                (c, v) -> c.global().setBadgeKnowYourBodyThreshold((Long) v)));
        list.add(new Property(PropertyNames.BADGE_CYCLE_VETERAN_THRESHOLD, PropertyNames.GROUP_BADGES,
                "Cycles required to earn the Cycle Veteran badge", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getBadgeCycleVeteranThreshold(),
                (c, v) -> c.global().setBadgeCycleVeteranThreshold((Long) v)));
        list.add(new Property(PropertyNames.BADGE_WEEK_WARRIOR_STREAK_THRESHOLD, PropertyNames.GROUP_BADGES,
                "Day streak required to earn the Week Warrior badge", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getBadgeWeekWarriorStreakThreshold(),
                (c, v) -> c.global().setBadgeWeekWarriorStreakThreshold((Long) v)));
        list.add(new Property(PropertyNames.INSIGHT_IRREGULAR_VARIATION_THRESHOLD_DAYS, PropertyNames.GROUP_INSIGHT,
                "Cycle-length variation, in days, flagged as irregular", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getInsightIrregularVariationThresholdDays(),
                (c, v) -> c.global().setInsightIrregularVariationThresholdDays((Long) v)));
        list.add(new Property(PropertyNames.INSIGHT_UNUSUAL_PERIOD_LENGTH_DELTA_DAYS, PropertyNames.GROUP_INSIGHT,
                "Period-length delta, in days, flagged as unusual", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getInsightUnusualPeriodLengthDeltaDays(),
                (c, v) -> c.global().setInsightUnusualPeriodLengthDeltaDays((Long) v)));
        list.add(new Property(PropertyNames.INSIGHT_DEFAULT_CYCLES, PropertyNames.GROUP_INSIGHT,
                "Cycles of history used to compute trends/regularity by default", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getInsightDefaultCycles(),
                (c, v) -> c.global().setInsightDefaultCycles((Long) v)));
        list.add(new Property(PropertyNames.SUBSCRIPTION_PERIOD_DAYS, PropertyNames.GROUP_BILLING,
                "Length of one billing period, in days", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getSubscriptionPeriodDays(),
                (c, v) -> c.global().setSubscriptionPeriodDays((Long) v)));
        list.add(new Property(PropertyNames.LOG_MAX_PERIOD_RANGE_DAYS, PropertyNames.GROUP_LOGS,
                "Max days a single period log entry may span", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getLogMaxPeriodRangeDays(),
                (c, v) -> c.global().setLogMaxPeriodRangeDays((Long) v)));
        list.add(new Property(PropertyNames.REWARDS_HISTORY_PAGE_SIZE, PropertyNames.GROUP_REWARDS,
                "Default page size for points history", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getRewardsHistoryPageSize(),
                (c, v) -> c.global().setRewardsHistoryPageSize((Long) v)));
        list.add(new Property(PropertyNames.CONTENT_FEED_DEFAULT_LIMIT, PropertyNames.GROUP_CONTENT,
                "Default number of items returned by the content feed", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getContentFeedDefaultLimit(),
                (c, v) -> c.global().setContentFeedDefaultLimit((Long) v)));

        list.sort(Comparator.comparing(Property::name));
        this.ordered = List.copyOf(list);

        Map<String, Property> index = new LinkedHashMap<>();
        for (Property property : this.ordered) {
            if (index.put(property.name(), property) != null) {
                throw new IllegalStateException("Duplicate config property in catalog: " + property.name());
            }
        }
        this.byName = Map.copyOf(index);
    }

    public List<Property> all() {
        return ordered;
    }

    public Optional<Property> find(String name) {
        return Optional.ofNullable(byName.get(name));
    }

    public List<Property> inGroup(String group) {
        return ordered.stream().filter(p -> p.group().equals(group)).toList();
    }

    public List<String> groups() {
        return ordered.stream().map(Property::group).distinct().sorted().toList();
    }
}
