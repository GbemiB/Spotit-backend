package com.spotit.api.spotlight.padbank.entity;

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
@Table(name = "padbank_runs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PadBankRun {
    public static final String PLAYING = "playing";
    public static final String DONE = "done";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "day_number", nullable = false)
    private int day;

    @Column(name = "visitor_ids", nullable = false, columnDefinition = "text")
    private String visitorIds;

    @Column(nullable = false)
    private int round;

    @Column(nullable = false)
    private int hearts;

    @Column(nullable = false)
    private int budget;

    @Column(nullable = false)
    private int served;

    @Column(name = "right_count", nullable = false)
    private int rightCount;

    @Column(nullable = false, columnDefinition = "text")
    private String stock;

    @Column(nullable = false, columnDefinition = "text")
    private String results;

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
