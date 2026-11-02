package com.spotit.api.spotlight.core.dto;

public record RankView(
        String id,
        String name,
        int minStars,
        String nextName,
        Integer nextMinStars
) {
}
