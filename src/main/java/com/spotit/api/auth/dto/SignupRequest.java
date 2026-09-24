package com.spotit.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank(message = "is required") String firstName,
        @NotBlank(message = "is required") String lastName,
        @NotBlank @Email String email
) {
}
