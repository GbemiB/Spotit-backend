package com.spotit.api.common.crypto;

/** Reversible encryption for secrets that must be stored (not hashed) and later read back, e.g. SMTP passwords. */
public interface EncryptionService {

    String encrypt(String plaintext);

    String decrypt(String ciphertext);
}
