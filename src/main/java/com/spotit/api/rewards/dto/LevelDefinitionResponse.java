package com.spotit.api.rewards.dto;

// Client-facing shape (no id/sortOrder — the list is already returned in progression order).
public record LevelDefinitionResponse(String name, long pointsLow, long pointsHigh) {
}
