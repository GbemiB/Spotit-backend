package com.spotit.api.smtp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SaveSmtpSettingsRequest(
        @NotBlank String host,
        @Min(1) @Max(65535) int port,
        @NotBlank String username,
        String password,
        @NotBlank String fromAddress,
        boolean useTls
) {
}
