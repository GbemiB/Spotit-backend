package com.spotit.api.auth.dto;

public record TokenResponse(String accessToken, String refreshToken, long expiresIn, UserSummary user) {

    public record UserSummary(java.util.UUID userId, boolean onboarded) {
    }
}
