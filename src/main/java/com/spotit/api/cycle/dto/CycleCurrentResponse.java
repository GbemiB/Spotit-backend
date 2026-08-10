package com.spotit.api.cycle.dto;

import java.time.LocalDate;

public record CycleCurrentResponse(
        Integer cycleDay,
        String phase,
        LocalDate nextPeriodDate,
        Long daysUntilNextPeriod,
        String confidence
) {
}
