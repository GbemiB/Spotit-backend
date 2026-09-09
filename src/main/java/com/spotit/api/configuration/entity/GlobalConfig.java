package com.spotit.api.configuration.entity;

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

// Single-row table (id is always SINGLETON_ID) holding every remaining app-wide numeric setting
// and threshold — the ones that used to be individual name/value rows in global_configuration,
// grouped there as points/cycle/badges/insight/billing/account/logs/rewards/content. Each is now
// its own typed column. Security and SMTP settings moved to SecurityConfig / SmtpConfig.
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
