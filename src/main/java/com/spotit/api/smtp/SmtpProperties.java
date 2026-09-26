package com.spotit.api.smtp;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The SMTP relay used for all transactional mail, bound from {@code spotit.smtp.*} in application.yml.
 * Every field is a placeholder for an environment variable (SMTP_HOST, SMTP_PORT, SMTP_USERNAME,
 * SMTP_PASSWORD, SMTP_FROM_ADDRESS, SMTP_USE_TLS) entered per environment in the Render dashboard, so
 * no relay credentials live in source control or the database.
 */
@ConfigurationProperties(prefix = "spotit.smtp")
public record SmtpProperties(
        String host,
        int port,
        String username,
        String password,
        String fromAddress,
        boolean useTls
) {
    /** True once every value needed to authenticate and send has been provided. */
    public boolean isConfigured() {
        return hasText(host) && port > 0 && hasText(username) && hasText(password) && hasText(fromAddress);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
