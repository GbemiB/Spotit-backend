package com.spotit.api.rewards.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateChallengeDefinitionRequest(
        @NotBlank String id,
        @NotBlank String title,
        @Min(1) int reward,
        @Min(1) int total,
        @NotBlank @Pattern(regexp = "WEEKLY_LOG|STATIC") String type
) {
}
