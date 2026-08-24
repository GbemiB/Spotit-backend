package com.spotit.api.rewards.service;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Single place that mutates a user's SpotPoints balance and appends the
 * matching points_history row, so every earning/spending path (logging,
 * daily bonus, ads, shop redemption, challenges) stays consistent.
 */
public interface PointsWriteService {

    record LogPointsResult(int pointsAwarded, long newBalance, int streak, boolean isNewEntry) {
    }

    record DailyClaimResult(int pointsAwarded, long newBalance, boolean alreadyClaimedToday) {
    }

    record AdWatchResult(int pointsAwarded, long newBalance) {
    }

    /** Awards points and updates the logging streak — but only the first time a given (today's) date is saved. */
    LogPointsResult recordDailyLog(UUID userId, LocalDate logDate, boolean isNewEntry);

    /**
     * Awards points for logging a period — but only for the first period-log action in the
     * current calendar month, regardless of how many corrections/updates follow it that same
     * month (they still save fine, just without repeat points).
     */
    LogPointsResult recordPeriodLog(UUID userId);

    DailyClaimResult claimDaily(UUID userId);

    AdWatchResult watchAd(UUID userId);

    /** Generic award/spend used by shop redemption and challenge claiming; `delta` may be negative. */
    long adjust(UUID userId, int delta, String icon, String label);
}
