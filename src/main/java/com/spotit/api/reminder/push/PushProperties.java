package com.spotit.api.reminder.push;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Expo push service settings ({@code spotit.push.*}). The access token is only required when "enhanced security"
 * is switched on for the Expo project; without it sends go out unauthenticated, which Expo allows by default.
 */
@ConfigurationProperties(prefix = "spotit.push")
public record PushProperties(String baseUrl, String expoAccessToken) {
    public boolean hasAccessToken() {
        return expoAccessToken != null && !expoAccessToken.isBlank();
    }
}
