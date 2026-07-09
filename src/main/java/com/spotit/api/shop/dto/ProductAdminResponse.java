package com.spotit.api.shop.dto;

public record ProductAdminResponse(String id, String name, int cost, String minLevel, boolean premiumOnly, String icon, boolean active) {
}
