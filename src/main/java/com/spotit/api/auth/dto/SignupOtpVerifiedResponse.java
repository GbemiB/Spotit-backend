package com.spotit.api.auth.dto;

import java.util.UUID;

public record SignupOtpVerifiedResponse(UUID leadId, String email) {
}
