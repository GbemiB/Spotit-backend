package com.spotit.api.reminder.push;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class PushConfig {
    // The reminder job runs on the single scheduler thread, so a hung call to Expo must not be able to stall it.
    @Bean
    RestClientCustomizer pushTimeouts() {
        return builder -> builder.requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(5))
                .withReadTimeout(Duration.ofSeconds(15))));
    }
}
