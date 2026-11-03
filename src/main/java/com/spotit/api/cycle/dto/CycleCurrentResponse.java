package com.spotit.api.cycle.dto;

import java.time.LocalDate;

public record CycleCurrentResponse(
        Integer cycleDay,
        String phase,
        LocalDate nextPeriodDate,
        Long daysUntilNextPeriod,
        String confidence,
        String status,
        Integer daysLate,
        LocalDate expectedDate,
        Integer missedCycles,
        LocalDate trackingStartDate
) {
    public static CycleCurrentResponse insufficientData() {
        return new CycleCurrentResponse(null, null, null, null, "insufficient_data", null, null, null, null, null);
    }
}
