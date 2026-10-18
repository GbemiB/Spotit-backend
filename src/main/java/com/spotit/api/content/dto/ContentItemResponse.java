package com.spotit.api.content.dto;

import java.util.List;

public record ContentItemResponse(String id, String tag, String title, String body, String imageKey, boolean sponsored, String advertiser,
                                  List<ContentReferenceDto> references) {
}
