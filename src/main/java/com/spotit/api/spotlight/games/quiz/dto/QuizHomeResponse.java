package com.spotit.api.spotlight.games.quiz.dto;

import java.util.List;

public record QuizHomeResponse(
        long balance,
        TodaysPick pick,
        List<TopicCard> topics,
        int topicCount,
        int rounds,
        int lives,
        int hintCost,
        int freeLevels,
        QuizResultTitles titles
) {
}
