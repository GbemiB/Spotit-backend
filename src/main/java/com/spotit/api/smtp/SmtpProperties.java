package com.spotit.api.smtp;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotit.smtp")
public record SmtpProperties(
        String host,
        int port,
        String username,
        String password,
        String fromAddress,
        boolean useTls
) {
    public boolean isConfigured() {
        return hasText(host) && port > 0 && hasText(username) && hasText(password) && hasText(fromAddress);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
