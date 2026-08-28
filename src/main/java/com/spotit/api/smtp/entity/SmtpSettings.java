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
 * At most one row per {@link SmtpRole} is expected to exist — {@code role=primary} is always
 * tried first by {@link com.spotit.api.common.mail.EmailServiceImpl}; {@code role=backup} (if
 * configured) is only tried when sending via primary throws. {@code password} is AES-GCM
 * ciphertext, never plaintext — see {@link com.spotit.api.common.crypto.EncryptionService}.
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

    // columnDefinition backfills the existing pre-role row (if any) as 'primary' when this
    // column is first added to an already-populated table, instead of failing/leaving it null.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20, columnDefinition = "varchar(20) default 'primary'")
    private SmtpRole role;

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
