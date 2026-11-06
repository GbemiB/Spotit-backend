package com.spotit.api.spotlight.games.quiz.dto;

import java.util.List;

public record QuizTopicsResponse(
        boolean underAge,
        int topicCount,
        List<GroupView> groups,
        List<GroupChip> allGroups,
        String nextCursor
) {
}
