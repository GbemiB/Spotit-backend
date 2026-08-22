package com.spotit.api.log.dto;

import java.time.LocalDate;
import java.util.List;

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
        int streak,
        // Days outside [startDate, endDate] whose stale flow (from before this edit moved or
        // shrank the period) got cleared — each entry is the day's corrected state (flow: null,
        // any other real data like mood/notes preserved). The caller must overwrite its local/
        // cached day-log state with these, or a UI like a calendar dot will keep showing them as
        // a period day. An entry with every field empty means the day had no other data and was
        // deleted outright — the caller should drop it from local state entirely rather than keep
        // a blank record.
        List<LogEntryResponse> clearedEntries
) {
}
