package com.spotit.api.log.dto;

import java.time.LocalDate;
import java.util.List;

public record LogEntryResponse(
        LocalDate date,
        String flow,
        String mood,
        List<String> symptoms,
        String notes,
        boolean intimate
) {
    public static LogEntryResponse empty(LocalDate date) {
        return new LogEntryResponse(date, null, null, List.of(), null, false);
    }
}
