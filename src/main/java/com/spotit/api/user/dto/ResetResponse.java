package com.spotit.api.user.dto;

import java.time.Instant;

public record ResetResponse(String message, Instant resetAt) {
}
