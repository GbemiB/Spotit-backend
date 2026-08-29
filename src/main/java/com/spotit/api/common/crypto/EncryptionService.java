package com.spotit.api.common.crypto;

public interface EncryptionService {
    String encrypt(String plaintext);

    String decrypt(String ciphertext);
}
