package com.spotit.api.content.dto;

import java.util.List;

public record ContentFeedResponse(List<ContentItemResponse> items) {
}
