package com.spotit.api.auth.dto;

import java.util.UUID;

// No userId: nothing is created yet but the lead — see SignupLead / AuthWriteService#completeSignup.
public record SignupResponse(UUID otpId, String email, long expiresInSeconds) {
}
