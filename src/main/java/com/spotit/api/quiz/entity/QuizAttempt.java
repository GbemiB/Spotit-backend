package com.spotit.api.quiz.entity;

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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quiz_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {
    public static final String PLAYING = "playing";
    public static final String CLEARED = "cleared";
    public static final String OUT = "out";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "topic_id", nullable = false, length = 40)
    private String topicId;

    @Column(name = "level_number", nullable = false)
    private int levelNumber;

    @Column(name = "question_ids", nullable = false, length = 1000)
    private String questionIds;

    @Column(nullable = false)
    private int round;

    @Column(nullable = false)
    private int lives;

    @Column(name = "start_lives")
    private Integer startLives;

    @Column(nullable = false)
    private int correct;

    @Column(nullable = false)
    private int streak;

    @Column(name = "best_streak", nullable = false)
    private int bestStreak;

    @Column(name = "hint_round", nullable = false)
    private int hintRound;

    @Column(name = "hints_used", nullable = false)
    private int hintsUsed;

    @Column(name = "sp_spent", nullable = false)
    private int spSpent;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(name = "played_on", nullable = false)
    private LocalDate playedOn;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    public List<String> questionIdList() {
        return Arrays.asList(questionIds.split(","));
    }
}
