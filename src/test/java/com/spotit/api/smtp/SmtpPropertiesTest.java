package com.spotit.api.smtp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmtpPropertiesTest {

    @Test
    void aCompleteRelayIsConfigured() {
        assertThat(new SmtpProperties("smtp.example.com", 587, "mailer", "s3cret", "no-reply@example.com", true).isConfigured()).isTrue();
    }

    @Test
    void theDefaultsAloneAreNotEnough() {
        assertThat(new SmtpProperties("smtp.gmail.com", 587, "", "", "", true).isConfigured()).isFalse();
    }

    @Test
    void everyRequiredValueMustBePresent() {
        assertThat(new SmtpProperties(null, 587, "mailer", "s3cret", "no-reply@example.com", true).isConfigured()).isFalse();
        assertThat(new SmtpProperties("smtp.example.com", 0, "mailer", "s3cret", "no-reply@example.com", true).isConfigured()).isFalse();
        assertThat(new SmtpProperties("smtp.example.com", 587, " ", "s3cret", "no-reply@example.com", true).isConfigured()).isFalse();
        assertThat(new SmtpProperties("smtp.example.com", 587, "mailer", null, "no-reply@example.com", true).isConfigured()).isFalse();
        assertThat(new SmtpProperties("smtp.example.com", 587, "mailer", "s3cret", "", true).isConfigured()).isFalse();
    }
}
