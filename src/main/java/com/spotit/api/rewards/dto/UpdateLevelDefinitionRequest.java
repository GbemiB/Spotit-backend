package com.spotit.api.rewards.dto;

public record UpdateLevelDefinitionRequest(String name, Long pointsLow, Long pointsHigh, Integer sortOrder) {
}
