package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.configuration.ConfigPropertyCatalog;
import com.spotit.api.configuration.SmtpSeedProperties;
import com.spotit.api.configuration.entity.SmtpConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import com.spotit.api.configuration.repository.SmtpConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** The fresh-database SMTP seed in {@link ConfigurationDomainServiceImpl#seedDefaults()}. */
@ExtendWith(MockitoExtension.class)
class ConfigurationSeedTest {
    @Mock SecurityConfigRepository securityConfigRepository;
    @Mock SmtpConfigRepository smtpConfigRepository;
    @Mock GlobalConfigRepository globalConfigRepository;
    @Mock EncryptionService encryptionService;

    private ConfigurationDomainServiceImpl service(String smtpPassword) {
        return new ConfigurationDomainServiceImpl(securityConfigRepository, smtpConfigRepository, globalConfigRepository, encryptionService,
                new SmtpSeedProperties("smtp.gmail.com", 587, "mailer@example.com", smtpPassword, "mailer@example.com", true),
                new ConfigPropertyCatalog());
    }

    @Test
    void aFreshDatabaseIsSeededWithTheConfiguredRelay() {
        when(smtpConfigRepository.findById(SmtpConfig.SINGLETON_ID)).thenReturn(Optional.empty());
        // The seeder also encrypts a freshly generated JWT secret, so stub encryption for any input.
        when(encryptionService.encrypt(any())).thenAnswer(inv -> "enc:" + inv.getArgument(0));

        service("app-password").seedDefaults();

        ArgumentCaptor<SmtpConfig> saved = ArgumentCaptor.forClass(SmtpConfig.class);
        verify(smtpConfigRepository).save(saved.capture());
        assertThat(saved.getValue().getHost()).isEqualTo("smtp.gmail.com");
        assertThat(saved.getValue().getPassword()).isEqualTo("enc:app-password");
    }

    @Test
    void withoutAnSmtpPasswordTheSeedIsSkippedAndStartupStillSucceeds() {
        when(smtpConfigRepository.findById(SmtpConfig.SINGLETON_ID)).thenReturn(Optional.empty());

        service("").seedDefaults();

        verify(smtpConfigRepository, never()).save(any());
    }

    @Test
    void anAlreadyConfiguredRelayIsLeftAlone() {
        SmtpConfig existing = SmtpConfig.builder().id(SmtpConfig.SINGLETON_ID).host("smtp.example.com").password("enc").build();
        when(smtpConfigRepository.findById(SmtpConfig.SINGLETON_ID)).thenReturn(Optional.of(existing));

        service("app-password").seedDefaults();

        verify(smtpConfigRepository, never()).save(any());
        assertThat(existing.getHost()).isEqualTo("smtp.example.com");
    }
}
