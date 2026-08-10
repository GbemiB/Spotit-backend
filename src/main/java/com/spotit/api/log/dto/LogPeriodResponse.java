package com.spotit.api.log.dto;

import java.time.LocalDate;

public record LogPeriodResponse(
        LocalDate startDate,
        LocalDate endDate,
        String flow,
        LocalDate lastPeriodDate,
        int cycleLength,
        int periodLength,
        LogEntryResponse startDayEntry,
        long pointsAwarded,
        long newBalance,
        int streak
) {
}
