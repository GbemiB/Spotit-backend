package com.spotit.api.cycle.dto;

import java.util.List;

public record CycleCalendarResponse(int year, int month, List<DayPhase> days) {

    public record DayPhase(String date, String phase) {
    }
}
