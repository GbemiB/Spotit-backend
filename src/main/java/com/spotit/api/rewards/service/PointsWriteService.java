package com.spotit.api.rewards.service;

import java.time.LocalDate;
import java.util.UUID;

public interface PointsWriteService {
    record LogPointsResult(int pointsAwarded, long newBalance, int streak, boolean isNewEntry) {
    }

    record DailyClaimResult(int pointsAwarded, long newBalance, boolean alreadyClaimedToday) {
    }

    record AdWatchResult(int pointsAwarded, long newBalance) {
    }

    LogPointsResult recordDailyLog(UUID userId, LocalDate logDate, boolean isNewEntry);

    LogPointsResult recordPeriodLog(UUID userId);

    DailyClaimResult claimDaily(UUID userId);

    AdWatchResult watchAd(UUID userId);

    long adjust(UUID userId, int delta, String icon, String label);
}
