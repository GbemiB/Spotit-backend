package com.spotit.api.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(
        @NotBlank String id,
        @NotBlank String name,
        @Min(1) int cost,
        @NotBlank String minLevel,
        boolean premiumOnly,
        String icon
) {
}
