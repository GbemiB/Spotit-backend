package com.spotit.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetOtpVerifyRequest(
        @NotBlank @Email String email,
        @NotBlank String code
) {
}
