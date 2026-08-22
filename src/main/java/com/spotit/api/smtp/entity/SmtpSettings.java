package com.spotit.api.smtp.entity;

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
 * Admin-configurable SMTP relay settings, stored so mail can be reconfigured without a redeploy.
 * Exactly one row is expected to exist at a time; {@code password} is AES-GCM ciphertext, never
 * plaintext — see {@link com.spotit.api.common.crypto.EncryptionService}.
 */
@Entity
@Table(name = "smtp_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmtpSettings {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String host;

    @Column(nullable = false)
    private int port;

    @Column(nullable = false)
    private String username;

    @Column(name = "encrypted_password", nullable = false, length = 1024)
    private String encryptedPassword;

    @Column(name = "from_address", nullable = false)
    private String fromAddress;

    @Column(name = "use_tls", nullable = false)
    private boolean useTls;

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
