package com.spotit.api.log.dto;

import java.time.LocalDate;
import java.util.List;

public record LogPeriodResponse(
        LocalDate startDate,
        LocalDate endDate,
        String flow,
        LocalDate lastPeriodDate,
        LocalDate trackingStartDate,
        int cycleLength,
        int periodLength,
        LogEntryResponse startDayEntry,
        long pointsAwarded,
        long newBalance,
        int streak,

        List<LogEntryResponse> clearedEntries
) {
}
