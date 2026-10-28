package com.spotit.api.quiz.dto;

import java.util.List;

public record QuizHomeResponse(
        long balance,
        QuizStats stats,
        TodaysPick pick,
        List<TopicCard> topics,
        int topicCount,
        int rounds,
        int lives,
        int hintCost
) {
}
