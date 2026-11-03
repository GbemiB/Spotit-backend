package com.spotit.api.monitoring.dto;

import java.time.Instant;
import java.util.UUID;

public record CrashItem(UUID id, UUID userId, String message, String stack, String screen, String appVersion, String platform,
                        String deviceModel, String osVersion, Instant happenedAt) {
}
