package com.spotit.api.content.dto;

public record ContentItemResponse(String id, String tag, String title, String imageUrl, boolean sponsored, String advertiser) {
}
