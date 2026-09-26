package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.configuration.ConfigDefaults;
import com.spotit.api.configuration.ConfigPropertyCatalog;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** The startup seeder in {@link ConfigurationDomainServiceImpl#seedDefaults()}. */
@ExtendWith(MockitoExtension.class)
class ConfigurationSeedTest {
    @Mock SecurityConfigRepository securityConfigRepository;
    @Mock GlobalConfigRepository globalConfigRepository;
    @Mock EncryptionService encryptionService;

    ConfigurationDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ConfigurationDomainServiceImpl(securityConfigRepository, globalConfigRepository, encryptionService, new ConfigPropertyCatalog());
    }

    @Test
    void aFreshDatabaseGetsAGeneratedJwtSecretAndEveryDefault() {
        when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.empty());
        when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.empty());
        when(encryptionService.encrypt(anyString())).thenAnswer(inv -> "enc:" + inv.getArgument(0));

        service.seedDefaults();

        ArgumentCaptor<SecurityConfig> security = ArgumentCaptor.forClass(SecurityConfig.class);
        verify(securityConfigRepository).save(security.capture());
        assertThat(security.getValue().getJwtSecret()).startsWith("enc:").hasSizeGreaterThan(40);
        assertThat(security.getValue().getJwtAccessTokenTtlSeconds()).isEqualTo(ConfigDefaults.JWT_ACCESS_TOKEN_TTL_SECONDS);
        assertThat(security.getValue().getOtpTtlSeconds()).isEqualTo(ConfigDefaults.OTP_TTL_SECONDS);

        ArgumentCaptor<GlobalConfig> global = ArgumentCaptor.forClass(GlobalConfig.class);
        verify(globalConfigRepository).save(global.capture());
        assertThat(global.getValue().getAdsDailyLimit()).isEqualTo(ConfigDefaults.ADS_DAILY_LIMIT);
        assertThat(global.getValue().getCycleDefaultLength()).isEqualTo(ConfigDefaults.CYCLE_DEFAULT_LENGTH);
        assertThat(global.getValue().getContentFeedDefaultLimit()).isEqualTo(ConfigDefaults.CONTENT_FEED_DEFAULT_LIMIT);
    }

    @Test
    void valuesAnAdminAlreadyTunedAreLeftAlone() {
        SecurityConfig security = SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).jwtSecret("existing")
                .jwtAccessTokenTtlSeconds(60).jwtRefreshTokenTtlSeconds(120).otpTtlSeconds(30).build();
        GlobalConfig global = GlobalConfig.builder().id(GlobalConfig.SINGLETON_ID).adsDailyLimit(9).cycleDefaultLength(31).build();
        when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(security));
        when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(global));

        service.seedDefaults();

        assertThat(security.getJwtSecret()).isEqualTo("existing");
        assertThat(security.getJwtAccessTokenTtlSeconds()).isEqualTo(60);
        assertThat(security.getOtpTtlSeconds()).isEqualTo(30);
        assertThat(global.getAdsDailyLimit()).isEqualTo(9);
        assertThat(global.getCycleDefaultLength()).isEqualTo(31);
        verify(encryptionService, never()).encrypt(anyString());
    }
}
