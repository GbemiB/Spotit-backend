package com.spotit.api.rewards.dto;

import java.time.LocalDate;

public record PointsHistoryEntryResponse(String icon, String label, int delta, LocalDate date) {
}
