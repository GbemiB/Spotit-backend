package com.spotit.api.smtp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// password is nullable on purpose: omit it to edit host/port/etc. without touching the
// previously stored (encrypted) password — see SmtpSettingsService#saveSettings.
public record SaveSmtpSettingsRequest(
        @NotBlank String host,
        @Min(1) @Max(65535) int port,
        @NotBlank String username,
        String password,
        @NotBlank String fromAddress,
        boolean useTls
) {
}
