package com.spotit.api.quiz.dto;

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
