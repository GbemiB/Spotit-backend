package com.spotit.api.auth.dto;

import java.util.UUID;

public record SignupResponse(UUID userId, String email, boolean otpRequired, UUID otpId, long expiresInSeconds) {
}
