package com.spotit.api.auth.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OtpResendRequest(@NotNull UUID otpId) {
}
