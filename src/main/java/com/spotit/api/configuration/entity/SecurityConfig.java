package com.spotit.api.configuration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

// Single-row table (id is always SINGLETON_ID) holding every setting in the "security" group.
// Split out of the former one-size-fits-all global_configuration table so each value is a typed
// column rather than a name/string_value pair. jwt-secret is stored encrypted (see
// EncryptionService); crypto-aes-key is plaintext by necessity — it can't be encrypted with
// itself — and is managed exclusively by AesGcmEncryptionService.
@Entity
@Table(name = "security_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityConfig {
    public static final int SINGLETON_ID = 1;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "jwt_secret", length = 2048)
    private String jwtSecret;

    @Column(name = "jwt_access_token_ttl_seconds", nullable = false)
    private long jwtAccessTokenTtlSeconds;

    @Column(name = "jwt_refresh_token_ttl_seconds", nullable = false)
    private long jwtRefreshTokenTtlSeconds;

    @Column(name = "otp_ttl_seconds", nullable = false)
    private long otpTtlSeconds;

    @Column(name = "crypto_aes_key", length = 2048)
    private String cryptoAesKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
