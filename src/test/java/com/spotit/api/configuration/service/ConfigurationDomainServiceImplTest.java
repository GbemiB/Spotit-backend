package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.ConfigPropertyCatalog;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.SmtpSeedProperties;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.entity.SmtpConfig;
import com.spotit.api.configuration.repository.GlobalConfigRepository;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import com.spotit.api.configuration.repository.SmtpConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationDomainServiceImplTest {
    @Mock SecurityConfigRepository securityConfigRepository;
    @Mock SmtpConfigRepository smtpConfigRepository;
    @Mock GlobalConfigRepository globalConfigRepository;
    @Mock EncryptionService encryptionService;

    ConfigurationDomainServiceImpl service;

    private static UpdateGlobalConfigurationRequest request(String groupName, Long value, Boolean enabled, String stringValue, String description) {
        return new UpdateGlobalConfigurationRequest(groupName, enabled, value, null, stringValue, description);
    }

    @BeforeEach
    void setUp() {
        service = new ConfigurationDomainServiceImpl(securityConfigRepository, smtpConfigRepository, globalConfigRepository,
                encryptionService,
                new SmtpSeedProperties("smtp.gmail.com", 587, "seed@example.com", "seed-password", "seed@example.com", true),
                new ConfigPropertyCatalog());
    }

    @Test
    void updateRejectsAnyChangeToTheEncryptionKeysStringValue() {
        UpdateGlobalConfigurationRequest req = request(null, null, null, "attacker-supplied-key", null);

        assertThatThrownBy(() -> service.update(PropertyNames.CRYPTO_AES_KEY, req))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.VALIDATION_ERROR);
        verify(securityConfigRepository, never()).save(any());
    }

    @Test
    void updateOnTheEncryptionKeyRowWithNoStringValueChangeIsAllowed() {
        SecurityConfig row = SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).cryptoAesKey("real-key").build();
        when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(row));
        when(securityConfigRepository.save(any(SecurityConfig.class))).thenAnswer(inv -> inv.getArgument(0));

        GlobalConfigurationResponse response = service.update(PropertyNames.CRYPTO_AES_KEY, request(null, null, null, null, "new description"));

        assertThat(response.name()).isEqualTo(PropertyNames.CRYPTO_AES_KEY);
        assertThat(response.groupName()).isEqualTo(PropertyNames.GROUP_SECURITY);
        assertThat(response.stringValue()).isNull();
    }

    @Test
    void getByNameRedactsTheEncryptionKeysStringValue() {
        when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID))
                .thenReturn(Optional.of(SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).cryptoAesKey("real-key-material").build()));

        GlobalConfigurationResponse response = service.getByName(PropertyNames.CRYPTO_AES_KEY);

        assertThat(response.stringValue()).isNull();
    }

    @Test
    void getByNameRedactsAnEncryptedSecretsStringValue() {
        when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID))
                .thenReturn(Optional.of(SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).jwtSecret("ciphertext").build()));

        GlobalConfigurationResponse response = service.getByName(PropertyNames.JWT_SECRET);

        assertThat(response.stringValue()).isNull();
    }

    @Test
    void getByNameDoesNotRedactAnOrdinaryProperty() {
        when(smtpConfigRepository.findById(SmtpConfig.SINGLETON_ID))
                .thenReturn(Optional.of(SmtpConfig.builder().id(SmtpConfig.SINGLETON_ID).host("smtp.example.com").build()));

        GlobalConfigurationResponse response = service.getByName(PropertyNames.SMTP_HOST);

        assertThat(response.stringValue()).isEqualTo("smtp.example.com");
    }

    @Test
    void updateOnAnEncryptedSecretEncryptsTheNewValueBeforeStorage() {
        SecurityConfig row = SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).build();
        when(securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(row));
        when(encryptionService.encrypt("new-secret")).thenReturn("encrypted-new-secret");
        when(securityConfigRepository.save(any(SecurityConfig.class))).thenAnswer(inv -> inv.getArgument(0));

        service.update(PropertyNames.JWT_SECRET, request(null, null, null, "new-secret", null));

        assertThat(row.getJwtSecret()).isEqualTo("encrypted-new-secret");
    }

    @Test
    void updateIgnoresAttemptsToReassignAPropertysGroup() {
        when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID))
                .thenReturn(Optional.of(GlobalConfig.builder().id(GlobalConfig.SINGLETON_ID).pointsDailyClaim(50).build()));
        when(globalConfigRepository.save(any(GlobalConfig.class))).thenAnswer(inv -> inv.getArgument(0));

        GlobalConfigurationResponse response = service.update(PropertyNames.POINTS_DAILY_CLAIM, request("rewards", null, null, null, null));

        assertThat(response.groupName()).isEqualTo(PropertyNames.GROUP_POINTS);
    }

    @Test
    void updateChangesALongValuedProperty() {
        GlobalConfig row = GlobalConfig.builder().id(GlobalConfig.SINGLETON_ID).contentFeedDefaultLimit(10).build();
        when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID)).thenReturn(Optional.of(row));
        when(globalConfigRepository.save(any(GlobalConfig.class))).thenAnswer(inv -> inv.getArgument(0));

        GlobalConfigurationResponse response = service.update(PropertyNames.CONTENT_FEED_DEFAULT_LIMIT, request(null, 25L, null, null, null));

        assertThat(row.getContentFeedDefaultLimit()).isEqualTo(25L);
        assertThat(response.value()).isEqualTo(25L);
    }

    @Test
    void listAllIsOrderedByNameAndMapsEachProperty() {
        when(globalConfigRepository.findById(GlobalConfig.SINGLETON_ID))
                .thenReturn(Optional.of(GlobalConfig.builder().id(GlobalConfig.SINGLETON_ID).logMaxPeriodRangeDays(14).build()));

        List<GlobalConfigurationResponse> all = service.listAll();

        assertThat(all).isSortedAccordingTo(Comparator.comparing(GlobalConfigurationResponse::name));
        GlobalConfigurationResponse logs = all.stream()
                .filter(r -> r.name().equals(PropertyNames.LOG_MAX_PERIOD_RANGE_DAYS))
                .findFirst().orElseThrow();
        assertThat(logs.groupName()).isEqualTo(PropertyNames.GROUP_LOGS);
        assertThat(logs.value()).isEqualTo(14L);
    }

    @Test
    void getByNameThrowsNotFoundForAnUnknownProperty() {
        assertThatThrownBy(() -> service.getByName("no-such-property"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
    }
}
