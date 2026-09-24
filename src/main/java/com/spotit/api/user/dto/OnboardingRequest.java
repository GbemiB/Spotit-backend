package com.spotit.api.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record OnboardingRequest(
        @NotNull LocalDate dob,
        LocalDate lastPeriodDate,
        @NotBlank String goal,
        @Min(21) @Max(45) Integer cycleLength,
        @Min(2) @Max(10) Integer periodLength
) {
}
