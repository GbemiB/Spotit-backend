package com.spotit.api.smtp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Boots the full application context against in-memory H2 and asserts that the SMTP relay comes from the
 * spotit.smtp.* placeholders (application.yml, with test values from application-test.yml) and that no
 * SMTP table exists in the database any more.
 */
@SpringBootTest
@ActiveProfiles("test")
class SmtpPropertiesContextTest {

    @Autowired
    SmtpProperties smtpProperties;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void smtpSettingsAreBoundFromTheEnvironmentPlaceholders() {
        assertThat(smtpProperties.host()).isEqualTo("smtp.gmail.com");
        assertThat(smtpProperties.port()).isEqualTo(587);
        assertThat(smtpProperties.useTls()).isTrue();
        assertThat(smtpProperties.username()).isNotBlank();
        assertThat(smtpProperties.password()).isNotBlank();
        assertThat(smtpProperties.fromAddress()).isNotBlank();
        assertThat(smtpProperties.isConfigured()).isTrue();
    }

    @Test
    void noSmtpTableIsCreatedInTheDatabase() {
        Integer tables = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables WHERE LOWER(table_name) IN ('smtp_config', 'smtp_settings')", Integer.class);

        assertThat(tables).isZero();
    }
}
