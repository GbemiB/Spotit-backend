package com.spotit.api.log.dto;

import java.time.LocalDate;
import java.util.List;

public record SaveLogResponse(
        LocalDate date,
        String flow,
        String mood,
        List<String> symptoms,
        String notes,
        boolean intimate,
        int pointsAwarded,
        long newBalance,
        int streak,
        boolean isNewEntry
) {
}
