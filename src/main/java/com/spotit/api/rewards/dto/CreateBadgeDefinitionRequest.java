package com.spotit.api.rewards.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBadgeDefinitionRequest(@NotBlank String id, @NotBlank String name, @NotBlank String description) {
}
