package com.spotit.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OtpVerifyRequest(@NotNull UUID otpId, @NotBlank String code) {
}
