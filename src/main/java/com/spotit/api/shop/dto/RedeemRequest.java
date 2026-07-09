package com.spotit.api.shop.dto;

import jakarta.validation.constraints.NotBlank;

public record RedeemRequest(@NotBlank String productId) {
}
