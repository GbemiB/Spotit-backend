package com.spotit.api.log.dto;

import java.time.LocalDate;
import java.util.List;

public record RemovePeriodResponse(
        LocalDate lastPeriodDate,
        LocalDate trackingStartDate,
        List<LogEntryResponse> clearedEntries
) {
}
