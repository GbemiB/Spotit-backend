package com.spotit.api.rewards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateLevelDefinitionRequest(
        @NotBlank String id,
        @NotBlank String name,
        @PositiveOrZero long pointsLow,
        @PositiveOrZero long pointsHigh,
        @PositiveOrZero int sortOrder
) {
}
