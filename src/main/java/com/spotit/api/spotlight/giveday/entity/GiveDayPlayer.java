package com.spotit.api.spotlight.giveday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "giveday_players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayPlayer {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "home_area", nullable = false, length = 30)
    private String homeArea;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
