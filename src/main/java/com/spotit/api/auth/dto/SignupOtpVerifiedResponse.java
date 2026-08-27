package com.spotit.api.auth.dto;

import java.util.UUID;

// Confirms the signup OTP checked out — no tokens yet, since no account exists until
// AuthWriteService#completeSignup is called with this leadId and a password.
public record SignupOtpVerifiedResponse(UUID leadId, String email) {
}
