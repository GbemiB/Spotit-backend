package com.spotit.api.content.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateContentItemRequest(
        @NotBlank String tag,
        @NotBlank String title,
        @NotBlank String body,
        String imageUrl,
        String imageKey,
        boolean sponsored,
        String advertiser,
        int sortOrder,
        Boolean active,
        List<@Valid ContentReferenceDto> references
) {
}
