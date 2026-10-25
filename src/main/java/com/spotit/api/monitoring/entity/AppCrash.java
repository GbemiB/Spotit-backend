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
@Table(name = "app_crashes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppCrash {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(length = 4000)
    private String stack;

    @Column(length = 60)
    private String screen;

    @Column(name = "app_version", length = 20)
    private String appVersion;

    @Column(length = 10)
    private String platform;

    @Column(name = "device_model", length = 80)
    private String deviceModel;

    @Column(name = "os_version", length = 20)
    private String osVersion;

    @Column(name = "happened_at", nullable = false)
    private Instant happenedAt;
}
