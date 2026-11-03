package com.spotit.api.spotlight.giveday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "giveday_runs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayRun {
    public static final String PLAYING = "playing";
    public static final String DONE = "done";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private int helped;

    @Column(name = "clock_minutes", nullable = false)
    private int clock;

    @Column(name = "home_area", length = 30)
    private String homeArea;

    @Column(name = "pos_x", nullable = false)
    private int posX;

    @Column(name = "pos_y", nullable = false)
    private int posY;

    @Column(name = "here_place", length = 40)
    private String here;

    @Column(length = 60)
    private String passes;

    @Column(nullable = false, columnDefinition = "text")
    private String visits;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(nullable = false)
    private int stars;

    @Column(name = "sp_spent", nullable = false)
    private int spSpent;

    @Column(name = "played_on", nullable = false)
    private LocalDate playedOn;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;
}
