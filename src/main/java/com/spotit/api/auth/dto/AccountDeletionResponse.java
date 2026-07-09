package com.spotit.api.auth.dto;

import java.time.Instant;

public record AccountDeletionResponse(String message, Instant purgeBy) {
}
