package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, UUID> {
    @Query("SELECT DISTINCT a.playedOn FROM QuizAttempt a WHERE a.userId = :userId AND a.status <> 'playing' AND a.playedOn >= :since")
    List<LocalDate> findPlayDaysSince(UUID userId, LocalDate since);
}
