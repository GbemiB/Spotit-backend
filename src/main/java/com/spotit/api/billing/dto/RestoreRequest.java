package com.spotit.api.billing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RestoreRequest(
        @NotBlank @Pattern(regexp = "ios|android") String platform,
        @NotBlank String receipt
) {
}
