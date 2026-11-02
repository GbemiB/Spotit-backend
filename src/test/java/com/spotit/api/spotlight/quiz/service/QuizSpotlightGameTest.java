package com.spotit.api.spotlight.quiz.service;

import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.quiz.entity.QuizLevelResult;
import com.spotit.api.spotlight.quiz.repository.QuizAttemptRepository;
import com.spotit.api.spotlight.quiz.repository.QuizLevelResultRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuizSpotlightGameTest {
    @Test
    void theQuizReportsStarsLevelsAnswersRunTopicsAndDays() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.of(2026, 11, 2);
        QuizLevelResultRepository results = mock(QuizLevelResultRepository.class);
        QuizAttemptRepository attempts = mock(QuizAttemptRepository.class);
        when(results.findByUserId(userId)).thenReturn(List.of(
                QuizLevelResult.builder().topicId("my-cycle").levelNumber(1).bestStars(3).build(),
                QuizLevelResult.builder().topicId("my-cycle").levelNumber(2).bestStars(0).build(),
                QuizLevelResult.builder().topicId("pcos").levelNumber(1).bestStars(2).build()));
        when(attempts.sumCorrect(userId)).thenReturn(12L);
        when(attempts.maxBestStreak(userId)).thenReturn(4);
        when(attempts.findPlayDaysSince(eq(userId), any())).thenReturn(List.of(today));

        QuizSpotlightGame game = new QuizSpotlightGame(results, attempts);
        GameSummary summary = game.summary(userId, today);

        assertThat(game.challengeId()).isEqualTo("know-yourself");
        assertThat(summary).extracting("stars", "levelsWon", "rightAnswers", "bestRun", "topicsStarted").containsExactly(5, 2, 12L, 4, 2);
        assertThat(summary.playDays()).containsExactly(today);
    }
}
