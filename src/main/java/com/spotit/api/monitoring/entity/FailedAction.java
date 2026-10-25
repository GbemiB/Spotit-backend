package com.spotit.api.monitoring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "failed_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedAction {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, length = 120)
    private String action;

    @Column(nullable = false)
    private int status;

    @Column(name = "happened_at", nullable = false)
    private Instant happenedAt;
}
