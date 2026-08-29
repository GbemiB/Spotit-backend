package com.spotit.api.content.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateContentItemRequest(
        @NotBlank String tag,
        @NotBlank String title,
        String body,
        String imageUrl,
        String imageKey,
        boolean sponsored,
        String advertiser,
        int sortOrder
) {
}
