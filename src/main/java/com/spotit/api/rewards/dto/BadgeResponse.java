package com.spotit.api.rewards.dto;

import java.time.Instant;

public record BadgeResponse(String id, String name, boolean earned, Instant earnedAt) {
}
