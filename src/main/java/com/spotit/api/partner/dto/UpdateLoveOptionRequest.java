package com.spotit.api.partner.dto;

import jakarta.validation.constraints.Size;

public record UpdateLoveOptionRequest(
        @Size(min = 1, max = 80) String label,
        @Size(max = 120) String sub,
        @Size(max = 16) String emoji,
        @Size(max = 60) String phrase,
        Integer sortOrder,
        Boolean active
) {
}
