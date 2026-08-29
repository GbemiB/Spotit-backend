package com.spotit.api.configuration.service;

import com.spotit.api.common.crypto.EncryptionService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.configuration.entity.GlobalConfiguration;
import com.spotit.api.configuration.repository.GlobalConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationDomainServiceImplTest {
    @Mock GlobalConfigurationRepository repository;
    @Mock EncryptionService encryptionService;

    ConfigurationDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ConfigurationDomainServiceImpl(repository, encryptionService);
    }

    @Test
    void updateRejectsAnyChangeToTheEncryptionKeysStringValue() {
        UpdateGlobalConfigurationRequest request = new UpdateGlobalConfigurationRequest(null, null, null, null, "attacker-supplied-key", null);

        assertThatThrownBy(() -> service.update(PropertyNames.CRYPTO_AES_KEY, request))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.VALIDATION_ERROR);
        verify(repository, never()).save(any());
    }

    @Test
    void updateAllowsNonStringValueChangesToTheEncryptionKeyRow() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.CRYPTO_AES_KEY).description("old").build();
        when(repository.findByName(PropertyNames.CRYPTO_AES_KEY)).thenReturn(Optional.of(row));
        when(repository.save(any(GlobalConfiguration.class))).thenAnswer(inv -> inv.getArgument(0));
        UpdateGlobalConfigurationRequest request = new UpdateGlobalConfigurationRequest(null, null, null, null, null, "new description");

        GlobalConfigurationResponse response = service.update(PropertyNames.CRYPTO_AES_KEY, request);

        assertThat(response.description()).isEqualTo("new description");
    }

    @Test
    void getByNameRedactsTheEncryptionKeysStringValue() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.CRYPTO_AES_KEY).stringValue("real-key-material").build();
        when(repository.findByName(PropertyNames.CRYPTO_AES_KEY)).thenReturn(Optional.of(row));

        GlobalConfigurationResponse response = service.getByName(PropertyNames.CRYPTO_AES_KEY);

        assertThat(response.stringValue()).isNull();
    }

    @Test
    void getByNameRedactsAnEncryptedSecretsStringValue() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.JWT_SECRET).stringValue("ciphertext").build();
        when(repository.findByName(PropertyNames.JWT_SECRET)).thenReturn(Optional.of(row));

        GlobalConfigurationResponse response = service.getByName(PropertyNames.JWT_SECRET);

        assertThat(response.stringValue()).isNull();
    }

    @Test
    void getByNameDoesNotRedactAnOrdinaryProperty() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.smtpHost("primary")).stringValue("smtp.example.com").build();
        when(repository.findByName(PropertyNames.smtpHost("primary"))).thenReturn(Optional.of(row));

        GlobalConfigurationResponse response = service.getByName(PropertyNames.smtpHost("primary"));

        assertThat(response.stringValue()).isEqualTo("smtp.example.com");
    }

    @Test
    void updateOnAnEncryptedSecretEncryptsTheNewValueBeforeStorage() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.JWT_SECRET).build();
        when(repository.findByName(PropertyNames.JWT_SECRET)).thenReturn(Optional.of(row));
        when(encryptionService.encrypt("new-secret")).thenReturn("encrypted-new-secret");
        when(repository.save(any(GlobalConfiguration.class))).thenAnswer(inv -> inv.getArgument(0));
        UpdateGlobalConfigurationRequest request = new UpdateGlobalConfigurationRequest(null, null, null, null, "new-secret", null);

        service.update(PropertyNames.JWT_SECRET, request);

        assertThat(row.getStringValue()).isEqualTo("encrypted-new-secret");
    }

    @Test
    void updateAllowsReassigningTheGroup() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.POINTS_DAILY_CLAIM).groupName("points").build();
        when(repository.findByName(PropertyNames.POINTS_DAILY_CLAIM)).thenReturn(Optional.of(row));
        when(repository.save(any(GlobalConfiguration.class))).thenAnswer(inv -> inv.getArgument(0));
        UpdateGlobalConfigurationRequest request = new UpdateGlobalConfigurationRequest("rewards", null, null, null, null, null);

        GlobalConfigurationResponse response = service.update(PropertyNames.POINTS_DAILY_CLAIM, request);

        assertThat(response.groupName()).isEqualTo("rewards");
    }

    @Test
    void listAllOrdersByNameAndMapsEachRow() {
        GlobalConfiguration row = GlobalConfiguration.builder().name(PropertyNames.LOG_MAX_PERIOD_RANGE_DAYS).groupName("logs").value(14L).build();
        when(repository.findAllByOrderByNameAsc()).thenReturn(List.of(row));

        List<GlobalConfigurationResponse> all = service.listAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).groupName()).isEqualTo("logs");
        assertThat(all.get(0).value()).isEqualTo(14L);
    }

    @Test
    void getByNameThrowsNotFoundForAnUnknownProperty() {
        when(repository.findByName("no-such-property")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByName("no-such-property"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
    }
}
