package com.spotit.api.partner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateLoveOptionRequest(
        @NotBlank @Pattern(regexp = "[a-z0-9_]{2,40}") String id,
        @NotBlank @Pattern(regexp = "promise|expression|starter|category") String kind,
        @NotBlank @Size(max = 80) String label,
        @Size(max = 120) String sub,
        @Size(max = 16) String emoji,
        @Size(max = 60) String phrase,
        Integer sortOrder,
        Boolean active
) {
}
