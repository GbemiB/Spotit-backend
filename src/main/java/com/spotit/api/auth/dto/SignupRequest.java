package com.spotit.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// No password here by design: an account (and its login-capable password) is only created
// once the OTP this issues has been verified — see AuthWriteService#completeSignup.
public record SignupRequest(
        @NotBlank(message = "is required") String firstName,
        @NotBlank(message = "is required") String lastName,
        @NotBlank @Email String email
) {
}
