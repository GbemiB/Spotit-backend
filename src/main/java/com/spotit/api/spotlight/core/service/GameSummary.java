package com.spotit.api.spotlight.core.service;

import java.time.LocalDate;
import java.util.Set;

public record GameSummary(int stars, int levelsWon, long rightAnswers, int bestRun, int topicsStarted, Set<LocalDate> playDays, long plays) {
    public static GameSummary empty() {
        return new GameSummary(0, 0, 0, 0, 0, Set.of(), 0);
    }
}
