package com.spotit.api.log.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cycle_logs", uniqueConstraints = @UniqueConstraint(name = "uq_cycle_logs_user_date", columnNames = {"user_id", "log_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CycleLog {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    private FlowIntensity flow;

    @Enumerated(EnumType.STRING)
    private MoodType mood;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "integer[]", nullable = false)
    private List<Integer> symptoms = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String notes;

    @Column(nullable = false)
    private boolean intimate;

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
