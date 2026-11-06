package com.spotit.api.spotlight.games.quiz.dto;

public record TopicCard(
        String id,
        String name,
        String groupId,
        String tone,
        int minAge,
        boolean locked,
        boolean started,
        int currentLevel,
        int levelCount,
        int levelsCleared,
        boolean ready,
        String icon
) {
}
