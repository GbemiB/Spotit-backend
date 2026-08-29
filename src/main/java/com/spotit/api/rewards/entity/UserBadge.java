package com.spotit.api.rewards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserBadge.Key.class)
public class UserBadge {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "badge_id", nullable = false, length = 40)
    private String badgeId;

    @Column(name = "earned_at", nullable = false)
    private Instant earnedAt;

    @PrePersist
    void onCreate() {
        if (earnedAt == null) earnedAt = Instant.now();
    }

    public record Key(UUID userId, String badgeId) implements Serializable {
    }
}
