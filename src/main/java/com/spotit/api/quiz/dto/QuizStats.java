package com.spotit.api.quiz.dto;

import java.util.List;

public record QuizStats(
        int streak,
        int stars,
        int levelsDone,
        long rightAnswers,
        int bestRun,
        int topicsStarted,
        List<Boolean> week,
        RankView rank
) {
}
