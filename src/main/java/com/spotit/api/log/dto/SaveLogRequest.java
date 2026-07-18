package com.spotit.api.log.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record SaveLogRequest(
        @Pattern(regexp = "spotting|light|medium|heavy") String flow,
        @Pattern(regexp = "happy|calm|energetic|neutral|sad|anxious|irritable|emotional") String mood,
        List<@Pattern(regexp = "cramps|headache|dizziness|bloating|tender|pelvicpain|fatigue|nausea|backpain|jointpain|acne|moodswings|insomnia|anxiety|discharge|sweating") String> symptoms,
        String notes,
        boolean intimate
) {
}
