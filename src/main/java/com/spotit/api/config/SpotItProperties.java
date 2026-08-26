package com.spotit.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code crypto.aes-key} is the only setting left here — it's the root key that encrypts secrets
 * stored in the DB (SMTP password, JWT signing secret — see {@code AppSettingsService} /
 * {@code SmtpSettingsService}), so it can never live in the DB itself. Everything else that used
 * to be config here now lives in the {@code app_settings} table.
 */
@ConfigurationProperties(prefix = "spotit")
public record SpotItProperties(Crypto crypto) {

    public record Crypto(String aesKey) {
    }
}
