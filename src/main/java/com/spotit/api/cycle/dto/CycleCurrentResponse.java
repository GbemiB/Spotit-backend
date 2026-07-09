package com.spotit.api.cycle.dto;

import java.time.LocalDate;

public record CycleCurrentResponse(
        int cycleDay,
        String phase,
        LocalDate nextPeriodDate,
        long daysUntilNextPeriod,
        String confidence
) {
}
