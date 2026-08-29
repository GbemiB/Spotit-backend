package com.spotit.api.common.crypto;

import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.entity.GlobalConfiguration;
import com.spotit.api.configuration.repository.GlobalConfigurationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES/GCM at-rest encryption for secrets like SMTP passwords. Each call generates a fresh random
 * IV and prepends it to the ciphertext ({@code iv || ciphertext+tag}, then Base64-encoded) so the
 * same plaintext never produces the same stored value twice and decrypt() has what it needs to
 * reverse it without a separate IV column.
 *
 * <p>The root key itself ({@code global_configuration.crypto-aes-key}) is read/self-seeded here,
 * as plaintext, directly via {@link GlobalConfigurationRepository} rather than through
 * {@code ConfigurationDomainService} — that service depends on this one to decrypt jwt-secret and
 * smtp-*-password, so going through it here would be circular. It can't be stored encrypted
 * either: a key encrypted with itself can't be decrypted without already knowing it. Storing the
 * root key in the same table as the values it protects means DB read access alone is enough to
 * decrypt everything in it — a deliberate tradeoff, not an oversight; see the git history for
 * this file for the discussion.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AesGcmEncryptionService implements EncryptionService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;
    // Same shape as `openssl rand -base64 32` — 32 random bytes is exactly AES-256.
    private static final int KEY_BYTES = 32;

    private final GlobalConfigurationRepository repository;
    private SecretKeySpec key;

    @PostConstruct
    void init() {
        GlobalConfiguration row = repository.findByName(PropertyNames.CRYPTO_AES_KEY).orElseGet(this::seedKey);
        String base64Key = row.getStringValue();
        byte[] decoded = Base64.getDecoder().decode(base64Key);
        if (decoded.length != KEY_BYTES) {
            throw new IllegalStateException(
                    "global_configuration.crypto-aes-key must decode to exactly 32 bytes (AES-256), got " + decoded.length);
        }
        key = new SecretKeySpec(decoded, "AES");
    }

    private GlobalConfiguration seedKey() {
        byte[] keyBytes = new byte[KEY_BYTES];
        new SecureRandom().nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        log.info("Seeded global_configuration.{} with a freshly generated AES-256 key.", PropertyNames.CRYPTO_AES_KEY);
        GlobalConfiguration row = GlobalConfiguration.builder()
                .name(PropertyNames.CRYPTO_AES_KEY)
                .groupName(PropertyNames.GROUP_SECURITY)
                .enabled(true)
                .stringValue(base64Key)
                .description("Root AES-256 key that encrypts every other secret in this table (jwt-secret, smtp-*-password). "
                        + "Plaintext by necessity — a key can't be encrypted with itself. Never change via the API: rotating it "
                        + "strands every secret already encrypted with the old value.")
                .build();
        return repository.save(row);
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
