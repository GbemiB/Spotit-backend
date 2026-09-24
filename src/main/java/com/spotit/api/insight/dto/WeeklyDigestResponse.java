package com.spotit.api.insight.dto;

import java.time.LocalDate;

public record WeeklyDigestResponse(int loggedCount, String topMood, LocalDate rangeStart, LocalDate rangeEnd) {
}
