package com.spotit.api.shop.dto;

import java.util.UUID;

public record RedeemResponse(UUID orderId, String productId, int pointsSpent, long newBalance, String status) {
}
