package com.spotit.api.auth.dto;

import java.util.UUID;

public record OtpRequestResponse(String message, UUID otpId, long expiresInSeconds) {
}
