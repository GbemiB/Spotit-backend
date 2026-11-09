package com.spotit.api.spotlight.games.quiz.dto;

public record AnswerResponse(
        int round,
        boolean right,
        String answer,
        String explanation,
        String funFact,
        int lives,
        int streak,
        int bestStreak,
        int correct,
        String status,
        int stars,
        Integer nextLevelOpened
) {
}
