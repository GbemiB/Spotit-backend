package com.spotit.api.quiz.dto;

import com.spotit.api.quiz.entity.QuizRank;

public record QuizRankAdmin(String id, String name, Integer minStars, Boolean active) {
    public static QuizRankAdmin of(QuizRank r) {
        return new QuizRankAdmin(r.getId(), r.getName(), r.getMinStars(), r.isActive());
    }
}
