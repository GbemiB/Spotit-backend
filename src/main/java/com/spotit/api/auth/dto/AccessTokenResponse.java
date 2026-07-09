package com.spotit.api.auth.dto;

public record AccessTokenResponse(String accessToken, long expiresIn) {
}
