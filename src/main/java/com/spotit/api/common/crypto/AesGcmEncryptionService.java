package com.spotit.api.common.crypto;

import com.spotit.api.configuration.entity.SecurityConfig;
import com.spotit.api.configuration.repository.SecurityConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

// @DependsOn: ConfigStoreMigration copies any pre-existing global_configuration.crypto-aes-key
// into security_config.crypto_aes_key before this bean initialises — without it, an
// already-deployed database would look keyless here and we'd generate a fresh key, stranding
// every secret already encrypted with the old one.
@Service
@Slf4j
@DependsOn("configStoreMigration")
@RequiredArgsConstructor
public class AesGcmEncryptionService implements EncryptionService {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private static final int KEY_BYTES = 32;

    private final SecurityConfigRepository securityConfigRepository;
    private SecretKeySpec key;

    @PostConstruct
    void init() {
        SecurityConfig row = securityConfigRepository.findById(SecurityConfig.SINGLETON_ID)
                .orElseGet(() -> SecurityConfig.builder().id(SecurityConfig.SINGLETON_ID).build());
        String base64Key = row.getCryptoAesKey();
        if (base64Key == null || base64Key.isBlank()) {
            base64Key = generateKey();
            row.setCryptoAesKey(base64Key);
            securityConfigRepository.save(row);
            log.info("Seeded security_config.crypto_aes_key with a freshly generated AES-256 key.");
        }
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        if (decoded.length != KEY_BYTES) {
            throw new IllegalStateException(
                    "security_config.crypto_aes_key must decode to exactly 32 bytes (AES-256), got " + decoded.length);
        }
        key = new SecretKeySpec(decoded, "AES");
    }

    private String generateKey() {
        byte[] keyBytes = new byte[KEY_BYTES];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    @Override
    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            SecureRandom.getInstanceStrong().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to encrypt value", e);
        }
    }

    @Override
    public String decrypt(String stored) {
        try {
            byte[] combined = Base64.getDecoder().decode(stored);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            byte[] ciphertext = new byte[combined.length - IV_LENGTH_BYTES];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH_BYTES);
            System.arraycopy(combined, IV_LENGTH_BYTES, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to decrypt value — wrong key or corrupted data", e);
        }
    }
}
