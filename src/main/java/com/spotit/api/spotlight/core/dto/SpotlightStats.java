package com.spotit.api.spotlight.core.dto;

import java.util.List;

public record SpotlightStats(
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
