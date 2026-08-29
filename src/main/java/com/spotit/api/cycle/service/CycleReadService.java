package com.spotit.api.cycle.service;

import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;

import java.util.UUID;

public interface CycleReadService {
    CycleCurrentResponse getCurrent(UUID userId);

    CycleCalendarResponse getCalendarMonth(UUID userId, int year, int month);
}
