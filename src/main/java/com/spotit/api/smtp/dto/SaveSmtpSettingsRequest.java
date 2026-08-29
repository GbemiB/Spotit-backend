package com.spotit.api.smtp.dto;

import com.spotit.api.smtp.entity.SmtpRole;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveSmtpSettingsRequest(
        @NotNull SmtpRole role,
        @NotBlank String host,
        @Min(1) @Max(65535) int port,
        @NotBlank String username,
        String password,
        @NotBlank String fromAddress,
        boolean useTls
) {
}
