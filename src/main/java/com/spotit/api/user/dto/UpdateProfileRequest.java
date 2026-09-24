package com.spotit.api.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        @Min(21) @Max(45) Integer cycleLength,
        @Min(2) @Max(10) Integer periodLength,
        @Pattern(regexp = "light|dark|system") String themePref
) {
}
