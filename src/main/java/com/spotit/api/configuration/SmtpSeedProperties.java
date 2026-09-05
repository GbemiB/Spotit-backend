package com.spotit.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Placeholder SMTP relay used to seed the smtp-* rows in global_configuration on a fresh database.
 * Bound from the {@code spotit.smtp.*} properties (see application.yml); every field is overridable
 * by an environment variable so the real relay/app-password never has to live in source control.
 */
@ConfigurationProperties(prefix = "spotit.smtp")
public record SmtpSeedProperties(
        String host,
        int port,
        String username,
        String password,
        String fromAddress,
        boolean useTls
) {
}
