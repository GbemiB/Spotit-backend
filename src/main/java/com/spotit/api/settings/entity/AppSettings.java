package com.spotit.api.settings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Admin-configurable app-wide settings (JWT TTLs, OTP TTL, ads/points economy, cycle defaults),
 * stored so they can be tuned without a redeploy. Exactly one row is expected to exist at a time —
 * see {@code AppSettingsServiceImpl}, which seeds it on first read. {@code encryptedJwtSecret} is
 * AES-GCM ciphertext, never plaintext — see {@link com.spotit.api.common.crypto.EncryptionService}.
 */
@Entity
@Table(name = "app_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSettings {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "encrypted_jwt_secret", nullable = false, length = 1024)
    private String encryptedJwtSecret;

    @Column(name = "jwt_access_token_ttl_seconds", nullable = false)
    private long jwtAccessTokenTtlSeconds;

    @Column(name = "jwt_refresh_token_ttl_seconds", nullable = false)
    private long jwtRefreshTokenTtlSeconds;

    @Column(name = "otp_ttl_seconds", nullable = false)
    private long otpTtlSeconds;

    @Column(name = "ads_daily_limit", nullable = false)
    private int adsDailyLimit;

    @Column(name = "cycle_default_length", nullable = false)
    private int cycleDefaultLength;

    @Column(name = "cycle_default_period_length", nullable = false)
    private int cycleDefaultPeriodLength;

    @Column(name = "points_daily_claim", nullable = false)
    private int pointsDailyClaim;

    @Column(name = "points_watch_ad", nullable = false)
    private int pointsWatchAd;

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
