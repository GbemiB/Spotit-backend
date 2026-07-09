package com.spotit.api.content.dto;

public record UpdateContentItemRequest(String tag, String title, String imageUrl, Boolean sponsored, String advertiser, Integer sortOrder) {
}
