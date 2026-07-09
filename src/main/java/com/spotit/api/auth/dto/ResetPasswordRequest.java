package com.spotit.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ResetPasswordRequest(
        @NotNull UUID otpId,
        @NotBlank String code,
        @NotBlank @Size(min = 8, message = "must be at least 8 characters") String newPassword
) {
}
