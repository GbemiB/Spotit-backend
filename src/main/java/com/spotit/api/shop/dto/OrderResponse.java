package com.spotit.api.shop.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderResponse(UUID orderId, String productId, String status, Instant createdAt) {
}
