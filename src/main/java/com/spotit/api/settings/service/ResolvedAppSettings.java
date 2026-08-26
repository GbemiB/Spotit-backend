package com.spotit.api.settings.service;

/** App settings with the JWT secret already decrypted — never persisted or logged in this form. */
public record ResolvedAppSettings(
        String jwtSecret,
        long jwtAccessTokenTtlSeconds,
        long jwtRefreshTokenTtlSeconds,
        long otpTtlSeconds,
        int adsDailyLimit,
        int cycleDefaultLength,
        int cycleDefaultPeriodLength,
        int pointsDailyClaim,
        int pointsWatchAd) {
}
