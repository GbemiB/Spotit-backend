package com.spotit.api.quiz.dto;

public record RankView(
        String id,
        String name,
        int minStars,
        String nextName,
        Integer nextMinStars
) {
}
