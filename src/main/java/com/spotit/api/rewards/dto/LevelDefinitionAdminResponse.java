package com.spotit.api.rewards.dto;

public record LevelDefinitionAdminResponse(String id, String name, long pointsLow, long pointsHigh, int sortOrder) {
}
