package com.spotit.api.billing.dto;

import java.time.Instant;

public record CancelResponse(boolean isPremium, boolean autoRenew, Instant accessUntil) {
}
