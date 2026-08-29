package com.spotit.api.content.dto;

public record UpdateContentItemRequest(String tag, String title, String body, String imageUrl, String imageKey, Boolean sponsored,
                                        String advertiser, Integer sortOrder) {
}
