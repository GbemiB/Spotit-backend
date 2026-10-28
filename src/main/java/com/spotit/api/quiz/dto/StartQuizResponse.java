package com.spotit.api.quiz.dto;

import java.util.List;
import java.util.UUID;

public record StartQuizResponse(
        UUID attemptId,
        String topicId,
        String topicName,
        int level,
        String levelName,
        List<RoundView> rounds,
        int lives,
        int hintCost,
        int spSpent,
        long balance
) {
}
