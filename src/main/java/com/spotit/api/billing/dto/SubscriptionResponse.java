package com.spotit.api.billing.dto;

import java.time.Instant;

public record SubscriptionResponse(boolean isPremium, String plan, Instant renewsAt, boolean autoRenew) {
}
