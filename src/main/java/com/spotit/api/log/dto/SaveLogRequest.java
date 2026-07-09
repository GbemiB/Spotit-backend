package com.spotit.api.log.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record SaveLogRequest(
        @Pattern(regexp = "spotting|light|medium|heavy") String flow,
        @Pattern(regexp = "happy|calm|energetic|neutral|sad|anxious|irritable|emotional") String mood,
        List<@Pattern(regexp = "cramps|headache|bloating|tender|fatigue|nausea|backpain|acne|moodswings|insomnia|discharge") String> symptoms,
        String notes,
        boolean intimate
) {
}
