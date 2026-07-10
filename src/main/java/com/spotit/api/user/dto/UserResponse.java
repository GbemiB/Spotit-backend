package com.spotit.api.user.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        LocalDate dob,
        String goal,
        int cycleLength,
        int periodLength,
        LocalDate lastPeriodDate,
        String themePref,
        boolean onboarded,
        boolean isPremium
) {
}
