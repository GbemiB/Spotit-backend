package com.spotit.api.log.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record LogPeriodRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        // Which day flow/mood/symptoms/notes/intimate actually describe — the client may be
        // logging from either boundary of the period, not just the start. Falls back to
        // startDate when omitted, matching the original single-boundary behavior.
        LocalDate detailDate,
        @Pattern(regexp = "spotting|light|medium|heavy") String flow,
        @Pattern(regexp = "happy|calm|energetic|neutral|sad|anxious|irritable|emotional") String mood,
        List<@Pattern(regexp = "cramps|headache|dizziness|bloating|tender|pelvicpain|fatigue|nausea|backpain|jointpain|acne|moodswings|insomnia|anxiety|discharge|sweating") String> symptoms,
        String notes,
        boolean intimate
) {
}
