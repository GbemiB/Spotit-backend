package com.spotit.api.spotlight.games.quiz.repository;

import com.spotit.api.spotlight.games.quiz.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
    @Query("SELECT DISTINCT a.playedOn FROM QuizAttempt a WHERE a.userId = :userId AND a.status <> 'playing' AND a.playedOn >= :since")
    List<LocalDate> findPlayDaysSince(UUID userId, LocalDate since);

    @Query("SELECT COALESCE(SUM(a.correct), 0) FROM QuizAttempt a WHERE a.userId = :userId")
    long sumCorrect(UUID userId);

    long countByUserId(UUID userId);

    @Query("SELECT COALESCE(MAX(a.bestStreak), 0) FROM QuizAttempt a WHERE a.userId = :userId")
    int maxBestStreak(UUID userId);
}
