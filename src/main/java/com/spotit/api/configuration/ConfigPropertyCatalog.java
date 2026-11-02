package com.spotit.api.configuration;

import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class ConfigPropertyCatalog {

    public enum Kind { LONG, STRING, BOOL }

    public enum Section { SECURITY, GLOBAL }

    public record Context(SecurityConfig security, GlobalConfig global) {
    }

    public record Property(String name, String group, String description, Kind kind, Section section,
                           boolean redacted, boolean encrypted,
                           Function<Context, Object> getter, BiConsumer<Context, Object> setter) {

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

        addSecurityProperties(list);
        addQuizProperties(list);
        addPadBankProperties(list);
        addGiveDayProperties(list);
        addGlobalProperties(list);

        this.ordered = List.copyOf(list);

        Map<String, Property> index = new LinkedHashMap<>();
        for (Property property : this.ordered) {
            if (index.put(property.name(), property) != null) {
                throw new IllegalStateException("Duplicate config property in catalog: " + property.name());
            }
        }
        this.byName = Map.copyOf(index);
    }

    private static void addSecurityProperties(List<Property> list) {
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
                "Root AES-256 key that encrypts every other secret (jwt-secret). Plaintext by "
                        + "necessity — a key can't be encrypted with itself. Never change via the API: rotating it "
                        + "strands every secret already encrypted with the old value.",
                Kind.STRING, Section.SECURITY, true, false,
                c -> c.security().getCryptoAesKey(),
                (c, v) -> c.security().setCryptoAesKey((String) v)));
    }

    private static void addGiveDayProperties(List<Property> list) {
        list.add(new Property(PropertyNames.GIVEDAY_START_COINS, PropertyNames.GROUP_GIVEDAY,
                "Give Day: give coins at the start of a day", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getGivedayStartCoins(),
                (c, v) -> c.global().setGivedayStartCoins((Long) v)));
        list.add(new Property(PropertyNames.GIVEDAY_GOOD_PICKS, PropertyNames.GROUP_GIVEDAY,
                "Give Day: well-matched products needed for the second star", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getGivedayGoodPicks(),
                (c, v) -> c.global().setGivedayGoodPicks((Long) v)));
        list.add(new Property(PropertyNames.GIVEDAY_TITLE_THREE, PropertyNames.GROUP_GIVEDAY,
                "Give Day: title for a give with three stars; unset uses A perfect give!",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getGivedayTitleThree(),
                (c, v) -> c.global().setGivedayTitleThree((String) v)));
        list.add(new Property(PropertyNames.GIVEDAY_TITLE_TWO, PropertyNames.GROUP_GIVEDAY,
                "Give Day: title for a give with two stars; unset uses Lovely work",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getGivedayTitleTwo(),
                (c, v) -> c.global().setGivedayTitleTwo((String) v)));
        list.add(new Property(PropertyNames.GIVEDAY_TITLE_ONE, PropertyNames.GROUP_GIVEDAY,
                "Give Day: title for a give with one star; unset uses A good start",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getGivedayTitleOne(),
                (c, v) -> c.global().setGivedayTitleOne((String) v)));
        list.add(new Property(PropertyNames.GIVEDAY_TITLE_ZERO, PropertyNames.GROUP_GIVEDAY,
                "Give Day: title for a give with no stars; unset uses Something important is missing",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getGivedayTitleZero(),
                (c, v) -> c.global().setGivedayTitleZero((String) v)));
    }

    private static void addPadBankProperties(List<Property> list) {
        list.add(new Property(PropertyNames.PADBANK_BUDGET, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: bank budget at the start of a day", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getPadbankBudget(),
                (c, v) -> c.global().setPadbankBudget((Long) v)));
        list.add(new Property(PropertyNames.PADBANK_HEARTS, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: hearts at the start of a day", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getPadbankHearts(),
                (c, v) -> c.global().setPadbankHearts((Long) v)));
        list.add(new Property(PropertyNames.PADBANK_DELIVERY_COST, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: budget a scooter delivery costs", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getPadbankDeliveryCost(),
                (c, v) -> c.global().setPadbankDeliveryCost((Long) v)));
        list.add(new Property(PropertyNames.PADBANK_BUDGET_GOAL, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: budget left needed for the third star", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getPadbankBudgetGoal(),
                (c, v) -> c.global().setPadbankBudgetGoal((Long) v)));
        list.add(new Property(PropertyNames.PADBANK_TITLE_GREAT, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: heading shown for right pack and route; unset uses PERFECT!",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getPadbankTitleGreat(),
                (c, v) -> c.global().setPadbankTitleGreat((String) v)));
        list.add(new Property(PropertyNames.PADBANK_TITLE_OK, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: heading shown for right pack, other route better; unset uses GOOD!",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getPadbankTitleOk(),
                (c, v) -> c.global().setPadbankTitleOk((String) v)));
        list.add(new Property(PropertyNames.PADBANK_TITLE_OOPS, PropertyNames.GROUP_PADBANK,
                "Pad Bank Run: heading shown for wrong pack; unset uses OOPS!",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getPadbankTitleOops(),
                (c, v) -> c.global().setPadbankTitleOops((String) v)));
    }

    private static void addQuizProperties(List<Property> list) {
        list.add(new Property(PropertyNames.QUIZ_ROUNDS, PropertyNames.GROUP_QUIZ,
                "Rounds (questions) in one Know Yourself quiz level", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getQuizRounds(),
                (c, v) -> c.global().setQuizRounds((Long) v)));
        list.add(new Property(PropertyNames.QUIZ_LIVES, PropertyNames.GROUP_QUIZ,
                "Hearts a player starts each quiz level with", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getQuizLives(),
                (c, v) -> c.global().setQuizLives((Long) v)));
        list.add(new Property(PropertyNames.QUIZ_HINT_COST, PropertyNames.GROUP_QUIZ,
                "SpotPoints charged for one quiz hint", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getQuizHintCost(),
                (c, v) -> c.global().setQuizHintCost((Long) v)));
        list.add(new Property(PropertyNames.SPOTLIGHT_INVITE_ENABLED, PropertyNames.GROUP_SPOTLIGHT,
                "Show the Spotlight invite pop-up on Home", Kind.BOOL, Section.GLOBAL, false, false,
                c -> !Boolean.FALSE.equals(c.global().getSpotlightInviteEnabled()),
                (c, v) -> c.global().setSpotlightInviteEnabled((Boolean) v)));
        list.add(new Property(PropertyNames.SPOTLIGHT_INVITE_EVERY_DAYS, PropertyNames.GROUP_SPOTLIGHT,
                "Days between Spotlight invite pop-ups for a user who has not played", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getSpotlightInviteEveryDays(),
                (c, v) -> c.global().setSpotlightInviteEveryDays((Long) v)));
        list.add(new Property(PropertyNames.SPOTLIGHT_INVITE_MAX_DISMISSALS, PropertyNames.GROUP_SPOTLIGHT,
                "Maybe-later taps in a row after which the Spotlight invite stops", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getSpotlightInviteMaxDismissals(),
                (c, v) -> c.global().setSpotlightInviteMaxDismissals((Long) v)));
        list.add(new Property(PropertyNames.QUIZ_TOPICS_PAGE_SIZE, PropertyNames.GROUP_QUIZ,
                "Topics sent per page on the Spotlight All topics screen", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getQuizTopicsPageSize(),
                (c, v) -> c.global().setQuizTopicsPageSize((Long) v)));
        list.add(new Property(PropertyNames.SPOTLIGHT_ADMIN_PAGE_SIZE, PropertyNames.GROUP_SPOTLIGHT,
                "Default page size for the Spotlight admin lists (challenges, ranks, quiz and game content)", Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getSpotlightAdminPageSize(),
                (c, v) -> c.global().setSpotlightAdminPageSize((Long) v)));
    }

    private static void addGlobalProperties(List<Property> list) {
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
        list.add(new Property(PropertyNames.APP_MIN_BUILD_ANDROID, PropertyNames.GROUP_APP,
                "Lowest Android build number (versionCode) allowed to run; older builds must update. 0 or unset turns the check off",
                Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getAppMinBuildAndroid(),
                (c, v) -> c.global().setAppMinBuildAndroid((Long) v)));
        list.add(new Property(PropertyNames.APP_MIN_BUILD_IOS, PropertyNames.GROUP_APP,
                "Lowest iOS build number allowed to run; older builds must update. 0 or unset turns the check off",
                Kind.LONG, Section.GLOBAL, false, false,
                c -> c.global().getAppMinBuildIos(),
                (c, v) -> c.global().setAppMinBuildIos((Long) v)));
        list.add(new Property(PropertyNames.APP_STORE_URL_ANDROID, PropertyNames.GROUP_APP,
                "Store page the Android update screen opens; unset uses the Google Play listing",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getAppStoreUrlAndroid(),
                (c, v) -> c.global().setAppStoreUrlAndroid((String) v)));
        list.add(new Property(PropertyNames.APP_SUPPORT_EMAIL, PropertyNames.GROUP_APP,
                "Email address the app’s Contact support row opens; unset uses spotitinternational@gmail.com",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getAppSupportEmail(),
                (c, v) -> c.global().setAppSupportEmail((String) v)));
        list.add(new Property(PropertyNames.APP_STORE_URL_IOS, PropertyNames.GROUP_APP,
                "Store page the iOS update screen opens; unset hides the update button on iOS",
                Kind.STRING, Section.GLOBAL, false, false,
                c -> c.global().getAppStoreUrlIos(),
                (c, v) -> c.global().setAppStoreUrlIos((String) v)));
        list.sort(Comparator.comparing(Property::name));
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
