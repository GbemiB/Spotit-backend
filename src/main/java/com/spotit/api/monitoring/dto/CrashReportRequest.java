package com.spotit.api.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CrashReportRequest(
        @NotBlank @Size(max = 2000) String message,
        @Size(max = 20000) String stack,
        @Size(max = 200) String screen,
        @Size(max = 40) String appVersion,
        @Size(max = 20) String platform,
        @Size(max = 200) String deviceModel,
        @Size(max = 40) String osVersion,
        Instant happenedAt
) {
}
