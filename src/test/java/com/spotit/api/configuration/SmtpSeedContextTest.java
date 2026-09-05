package com.spotit.api.configuration;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Boots the full application context against in-memory H2 and asserts that the @PostConstruct
 * seeder wrote the smtp-* rows into global_configuration from the spotit.smtp.* properties
 * (application.yml) — i.e. the SMTP details land in the DB with no source-hardcoded values.
 */
@SpringBootTest
@ActiveProfiles("test")
class SmtpSeedContextTest {

    @Autowired
    ConfigurationDomainService configurationDomainService;

    @Test
    void seedsSmtpSettingsFromPropertiesIntoTheDatabase() {
        Optional<ResolvedSmtpSettings> settings = configurationDomainService.getSmtpSettings();

        assertThat(settings).isPresent();
        assertThat(settings.get().host()).isEqualTo("smtp.gmail.com");
        assertThat(settings.get().port()).isEqualTo(587);
        assertThat(settings.get().username()).isEqualTo("oluwagbemisolabello@gmail.com");
        assertThat(settings.get().fromAddress()).isEqualTo("oluwagbemisolabello@gmail.com");
        assertThat(settings.get().useTls()).isTrue();
        assertThat(settings.get().password()).isEqualTo("jetpffwfyqaupfbg");
    }
}
