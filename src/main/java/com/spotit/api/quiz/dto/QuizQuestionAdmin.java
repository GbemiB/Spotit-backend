package com.spotit.api.quiz.dto;

import com.spotit.api.quiz.entity.QuizQuestion;

import java.time.Instant;

public record QuizQuestionAdmin(
        String id,
        String topicId,
        Integer levelNumber,
        String statement,
        String answer,
        String hint,
        String explanation,
        String funFact,
        String source,
        String status,
        String reviewedBy,
        Boolean active,
        Instant updatedAt
) {
    public static QuizQuestionAdmin of(QuizQuestion q) {
        return new QuizQuestionAdmin(q.getId(), q.getTopicId(), q.getLevelNumber(), q.getStatement(), q.getAnswer(), q.getHint(),
                q.getExplanation(), q.getFunFact(), q.getSource(), q.getStatus(), q.getReviewedBy(), q.isActive(), q.getUpdatedAt());
    }
}
