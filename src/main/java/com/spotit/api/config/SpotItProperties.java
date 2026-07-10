package com.spotit.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotit")
public record SpotItProperties(Jwt jwt, Otp otp, Ads ads, Cycle cycle, Mail mail) {

    public record Jwt(String secret, long accessTokenTtlSeconds, long refreshTokenTtlSeconds) {
    }

    public record Otp(long ttlSeconds) {
    }

    public record Ads(int dailyLimit) {
    }

    public record Cycle(int defaultCycleLength, int defaultPeriodLength) {
    }

    public record Mail(String fromAddress) {
    }
}
