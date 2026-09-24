package com.spotit.api.common.crypto;

import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AesGcmEncryptionServiceTest {
    @Mock SecurityConfigRepository repository;

    private AesGcmEncryptionService service() {
        return new AesGcmEncryptionService(repository);
    }

    @Test
    void initSelfSeedsAFreshKeyWhenNoneExistsAndCanRoundTrip() {
        when(repository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.empty());
        when(repository.save(any(SecurityConfig.class))).thenAnswer(inv -> inv.getArgument(0));
        AesGcmEncryptionService service = service();

        service.init();

        ArgumentCaptor<SecurityConfig> captor = ArgumentCaptor.forClass(SecurityConfig.class);
        verify(repository).save(captor.capture());
        SecurityConfig saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(SecurityConfig.SINGLETON_ID);
        assertThat(Base64.getDecoder().decode(saved.getCryptoAesKey())).hasSize(32);

        String ciphertext = service.encrypt("hello world");
        assertThat(ciphertext).isNotEqualTo("hello world");
        assertThat(service.decrypt(ciphertext)).isEqualTo("hello world");
    }

    @Test
    void initReusesAnExistingKeyWithoutOverwritingIt() {
        String existingKey = Base64.getEncoder().encodeToString(new byte[32]);
        SecurityConfig row = SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).cryptoAesKey(existingKey).build();
        when(repository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(row));
        AesGcmEncryptionService service = service();

        service.init();

        verify(repository, never()).save(any());
        String ciphertext = service.encrypt("reused key");
        assertThat(service.decrypt(ciphertext)).isEqualTo("reused key");
    }

    @Test
    void initRejectsAStoredKeyThatIsNotThirtyTwoBytes() {
        String badKey = Base64.getEncoder().encodeToString(new byte[16]);
        SecurityConfig row = SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).cryptoAesKey(badKey).build();
        when(repository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.of(row));
        AesGcmEncryptionService service = service();

        assertThatThrownBy(service::init).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void encryptingTheSamePlaintextTwiceProducesDifferentCiphertext() {
        when(repository.findById(SecurityConfig.SINGLETON_ID)).thenReturn(Optional.empty());
        when(repository.save(any(SecurityConfig.class))).thenAnswer(inv -> inv.getArgument(0));
        AesGcmEncryptionService service = service();
        service.init();

        String a = service.encrypt("same plaintext");
        String b = service.encrypt("same plaintext");

        assertThat(a).isNotEqualTo(b);
        assertThat(service.decrypt(a)).isEqualTo("same plaintext");
        assertThat(service.decrypt(b)).isEqualTo("same plaintext");
    }
}
