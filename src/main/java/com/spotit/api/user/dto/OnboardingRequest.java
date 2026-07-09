package com.spotit.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record OnboardingRequest(
        @NotNull LocalDate dob,
        LocalDate lastPeriodDate,
        @NotBlank @Pattern(regexp = "track|conceive|avoid|curious") String goal
) {
}
