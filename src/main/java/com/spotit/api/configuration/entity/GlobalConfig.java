package com.spotit.api.configuration.entity;

import com.spotit.api.configuration.ConfigDefaults;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "global_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalConfig {
    public static final int SINGLETON_ID = 1;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "ads_daily_limit", nullable = false)
    private long adsDailyLimit;

    @Column(name = "cycle_default_length", nullable = false)
    private long cycleDefaultLength;

    @Column(name = "cycle_default_period_length", nullable = false)
    private long cycleDefaultPeriodLength;

    @Column(name = "points_daily_claim", nullable = false)
    private long pointsDailyClaim;

    @Column(name = "points_watch_ad", nullable = false)
    private long pointsWatchAd;

    @Column(name = "quiz_rounds", nullable = false, columnDefinition = "bigint default 5")
    private long quizRounds;

    @Column(name = "quiz_lives", nullable = false, columnDefinition = "bigint default 3")
    private long quizLives;

    @Column(name = "quiz_hint_cost", nullable = false, columnDefinition = "bigint default 5")
    private long quizHintCost;

    @Column(name = "spotlight_invite_enabled", columnDefinition = "boolean default true")
    private Boolean spotlightInviteEnabled;

    @Column(name = "spotlight_invite_every_days", nullable = false, columnDefinition = "bigint default 3")
    private long spotlightInviteEveryDays;

    @Column(name = "spotlight_invite_max_dismissals", nullable = false, columnDefinition = "bigint default 3")
    private long spotlightInviteMaxDismissals;

    @Column(name = "quiz_topics_page_size", nullable = false, columnDefinition = "bigint default 8")
    private long quizTopicsPageSize;

    @Column(name = "spotlight_admin_page_size", nullable = false, columnDefinition = "bigint default 50")
    private long spotlightAdminPageSize;

    @Column(name = "giveday_day_start", nullable = false, columnDefinition = "bigint default 480")
    private long givedayDayStart;

    @Column(name = "giveday_day_end", nullable = false, columnDefinition = "bigint default 1080")
    private long givedayDayEnd;

    @Column(name = "giveday_visit_minutes", nullable = false, columnDefinition = "bigint default 30")
    private long givedayVisitMinutes;

    @Column(name = "giveday_units_per_km", nullable = false, columnDefinition = "bigint default 100")
    private long givedayUnitsPerKm;

    @Column(name = "giveday_good_picks", nullable = false, columnDefinition = "bigint default 3")
    private long givedayGoodPicks;

    @Column(name = "giveday_title_three", length = 60)
    private String givedayTitleThree;

    @Column(name = "giveday_title_two", length = 60)
    private String givedayTitleTwo;

    @Column(name = "giveday_title_one", length = 60)
    private String givedayTitleOne;

    @Column(name = "giveday_title_zero", length = 60)
    private String givedayTitleZero;

    @Column(name = "padbank_free_visitors", nullable = false, columnDefinition = "bigint default 2")
    @Builder.Default
    private long padbankFreeVisitors = ConfigDefaults.PADBANK_FREE_VISITORS;

    @Column(name = "padbank_free_days", nullable = false, columnDefinition = "bigint default 2")
    @Builder.Default
    private long padbankFreeDays = ConfigDefaults.PADBANK_FREE_DAYS;

    @Column(name = "padbank_hearts", nullable = false, columnDefinition = "bigint default 3")
    private long padbankHearts;

    @Column(name = "padbank_delivery_cost", nullable = false, columnDefinition = "bigint default 2")
    private long padbankDeliveryCost;

    @Column(name = "padbank_visitor_fee", nullable = false, columnDefinition = "bigint default 2")
    @Builder.Default
    private long padbankVisitorFee = ConfigDefaults.PADBANK_VISITOR_FEE;

    @Column(name = "padbank_title_great", length = 40)
    private String padbankTitleGreat;

    @Column(name = "padbank_title_ok", length = 40)
    private String padbankTitleOk;

    @Column(name = "padbank_title_oops", length = 40)
    private String padbankTitleOops;

    @Column(name = "quiz_title_won", length = 60)
    private String quizTitleWon;

    @Column(name = "quiz_title_perfect", length = 60)
    private String quizTitlePerfect;

    @Column(name = "quiz_title_lost", length = 60)
    private String quizTitleLost;

    @Column(name = "padbank_day_title_perfect", length = 60)
    private String padbankDayTitlePerfect;

    @Column(name = "padbank_day_title_done", length = 60)
    private String padbankDayTitleDone;

    @Column(name = "padbank_day_title_lost", length = 60)
    private String padbankDayTitleLost;

    @Column(name = "account_purge_grace_days", nullable = false)
    private long accountPurgeGraceDays;

    @Column(name = "badge_know_your_body_threshold", nullable = false)
    private long badgeKnowYourBodyThreshold;

    @Column(name = "badge_cycle_veteran_threshold", nullable = false)
    private long badgeCycleVeteranThreshold;

    @Column(name = "badge_week_warrior_streak_threshold", nullable = false)
    private long badgeWeekWarriorStreakThreshold;

    @Column(name = "cycle_high_confidence_log_threshold", nullable = false)
    private long cycleHighConfidenceLogThreshold;

    @Column(name = "insight_irregular_variation_threshold_days", nullable = false)
    private long insightIrregularVariationThresholdDays;

    @Column(name = "insight_unusual_period_length_delta_days", nullable = false)
    private long insightUnusualPeriodLengthDeltaDays;

    @Column(name = "insight_default_cycles", nullable = false)
    private long insightDefaultCycles;

    @Column(name = "subscription_period_days", nullable = false)
    private long subscriptionPeriodDays;

    @Column(name = "log_max_period_range_days", nullable = false)
    private long logMaxPeriodRangeDays;

    @Column(name = "rewards_history_page_size", nullable = false)
    private long rewardsHistoryPageSize;

    @Column(name = "content_feed_default_limit", nullable = false)
    private long contentFeedDefaultLimit;

    @Column(name = "app_min_build_android")
    private Long appMinBuildAndroid;

    @Column(name = "app_min_build_ios")
    private Long appMinBuildIos;

    @Column(name = "app_store_url_android")
    private String appStoreUrlAndroid;

    @Column(name = "app_support_email")
    private String appSupportEmail;

    @Column(name = "app_store_url_ios")
    private String appStoreUrlIos;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
