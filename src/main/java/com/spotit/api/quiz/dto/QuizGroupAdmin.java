package com.spotit.api.quiz.dto;

import com.spotit.api.quiz.entity.QuizGroup;

public record QuizGroupAdmin(String id, String label, String tone, Integer sortOrder, Boolean active) {
    public static QuizGroupAdmin of(QuizGroup g) {
        return new QuizGroupAdmin(g.getId(), g.getLabel(), g.getTone(), g.getSortOrder(), g.isActive());
    }
}
