package com.spotit.api.auth.dto;

import java.util.UUID;

public record SignupResponse(UUID otpId, String email, long expiresInSeconds) {
}
