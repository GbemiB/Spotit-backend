package com.spotit.api.content.dto;

import java.util.UUID;

public record ContentItemAdminResponse(UUID id, String tag, String title, String body, String imageUrl, String imageKey, boolean sponsored,
                                        String advertiser, int sortOrder) {
}
