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

// Single-row table (id is always SINGLETON_ID) holding the one SMTP relay used to send all
// transactional mail. Split out of the former global_configuration table so each field is a
// typed column. The password is stored encrypted (see EncryptionService).
@Entity
@Table(name = "smtp_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmtpConfig {
    public static final int SINGLETON_ID = 1;

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "host", length = 2048)
    private String host;

    @Column(name = "port", nullable = false)
    private int port;

    @Column(name = "username", length = 2048)
    private String username;

    @Column(name = "password", length = 2048)
    private String password;

    @Column(name = "from_address", length = 2048)
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
