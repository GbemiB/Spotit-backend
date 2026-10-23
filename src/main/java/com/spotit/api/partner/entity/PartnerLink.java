package com.spotit.api.partner.entity;

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

@Entity
@Table(name = "partner_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerLink {
    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "partner_id")
    private UUID partnerId;

    @Column(name = "invite_email", nullable = false)
    private String inviteEmail;

    @Column(name = "invite_code_hash", nullable = false)
    private String inviteCodeHash;

    @Column(name = "invite_expires_at", nullable = false)
    private Instant inviteExpiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PartnerLinkStatus status;

    @Column(name = "partner_nickname", length = 40)
    private String partnerNickname;

    @Column(name = "partner_reminder", nullable = false)
    private boolean partnerReminder;

    @Column(name = "failed_attempts", nullable = false, columnDefinition = "integer default 0")
    private int failedAttempts;

    @Column(name = "last_reminded_period_date")
    private LocalDate lastRemindedPeriodDate;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

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
