package com.spotit.api.shop.dto;

public record UpdateProductRequest(String name, Integer cost, String minLevel, Boolean premiumOnly, String icon, Boolean active) {
}
