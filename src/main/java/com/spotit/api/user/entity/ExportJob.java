package com.spotit.api.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "export_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportJob {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExportJobStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public enum ExportJobStatus {
        processing(1, "exportJobStatus.processing"),
        ready(2, "exportJobStatus.ready"),
        failed(3, "exportJobStatus.failed");

        private final Integer value;
        private final String code;

        ExportJobStatus(Integer value, String code) {
            this.value = value;
            this.code = code;
        }

        public static ExportJobStatus fromInt(Integer value) {
            return switch (value) {
                case 1 -> processing;
                case 2 -> ready;
                case 3 -> failed;
                default -> throw new IllegalArgumentException("Unknown ExportJobStatus value: " + value);
            };
        }

        public Integer getValue() {
            return value;
        }

        public String getCode() {
            return code;
        }

        public boolean isProcessing() {
            return this == processing;
        }

        public boolean isReady() {
            return this == ready;
        }

        public boolean isFailed() {
            return this == failed;
        }
    }
}
