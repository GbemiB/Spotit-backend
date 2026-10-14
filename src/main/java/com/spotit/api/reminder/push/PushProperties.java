package com.spotit.api.reminder.push;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spotit.push")
public record PushProperties(String baseUrl, String expoAccessToken) {
    public boolean hasAccessToken() {
        return expoAccessToken != null && !expoAccessToken.isBlank();
    }
}
