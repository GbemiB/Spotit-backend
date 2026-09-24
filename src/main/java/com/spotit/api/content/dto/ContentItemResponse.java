package com.spotit.api.content.dto;

public record ContentItemResponse(String id, String tag, String title, String body, String imageKey, boolean sponsored, String advertiser) {
}
