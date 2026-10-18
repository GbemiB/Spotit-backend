package com.spotit.api.content.dto;

import jakarta.validation.Valid;

import java.util.List;

public record UpdateContentItemRequest(String tag, String title, String body, String imageUrl, String imageKey, Boolean sponsored,
                                        String advertiser, Integer sortOrder, Boolean active, List<@Valid ContentReferenceDto> references) {
}
