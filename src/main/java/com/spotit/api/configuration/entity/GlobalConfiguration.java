package com.spotit.api.configuration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A single named row in the app-wide configuration store — replaces what used to be separate
 * fixed-column tables (app_settings, smtp_settings) and a handful of hardcoded Java constants
 * (badge-earning thresholds, account purge grace period, etc.) with one generic table, in the
 * style of Fineract's global configuration properties. Only the column matching the property's
 * actual type is populated; the rest stay null. {@code stringValue} holds AES-GCM ciphertext for
 * any property that's a secret (jwt-secret, smtp-*-password) — see
 * {@link com.spotit.api.common.crypto.EncryptionService}.
 */
@Entity
@Table(name = "global_configuration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalConfiguration {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    // Not a DB enum on purpose — see PropertyNames' GROUP_* constants for the fixed set this
    // codebase actually uses; free text here just avoids a migration every time a new group is added.
    @Column(name = "group_name", length = 50)
    private String groupName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "value")
    private Long value;

    @Column(name = "date_value")
    private LocalDate dateValue;

    @Column(name = "string_value", length = 2048)
    private String stringValue;

    @Column(name = "description", length = 500)
    private String description;

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
