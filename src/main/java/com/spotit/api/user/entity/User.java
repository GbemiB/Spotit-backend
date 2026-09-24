package com.spotit.api.user.entity;

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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 60)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "cycle_length", nullable = false)
    private int cycleLength;

    @Column(name = "period_length", nullable = false)
    private int periodLength;

    @Column(name = "last_period_date")
    private LocalDate lastPeriodDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_pref", nullable = false)
    private ThemePref themePref;

    @Column(nullable = false)
    private boolean onboarded;

    @Column(name = "notif_period", nullable = false)
    private boolean notifPeriod;

    @Column(name = "notif_ovulation", nullable = false)
    private boolean notifOvulation;

    @Column(name = "notif_daily_log", nullable = false)
    private boolean notifDailyLog;

    @Column(name = "notif_digest", nullable = false)
    private boolean notifDigest;

    @Column(nullable = false)
    private long points;

    @Column(nullable = false)
    private int streak;

    @Column(name = "longest_streak", nullable = false)
    private int longestStreak;

    @Column(name = "last_log_date")
    private LocalDate lastLogDate;

    @Column(name = "last_period_log_date")
    private LocalDate lastPeriodLogDate;

    @Column(name = "last_claimed_date")
    private LocalDate lastClaimedDate;

    @Column(name = "is_premium", nullable = false)
    private boolean premium;

    @Column(name = "pending_deletion_at")
    private Instant pendingDeletionAt;

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
