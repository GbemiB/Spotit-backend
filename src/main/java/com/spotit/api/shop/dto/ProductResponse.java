package com.spotit.api.shop.dto;

public record ProductResponse(
        String id, String name, int cost, String minLevel, boolean premiumOnly,
        boolean locked, String lockReason
) {
}
