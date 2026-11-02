package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.core.service.SpotlightGame;
import com.spotit.api.spotlight.padbank.repository.PadBankRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PadBankSpotlightGame implements SpotlightGame {
    static final int HISTORY_DAYS = 400;
    private final PadBankRunRepository runRepository;

    @Override
    public String challengeId() {
        return PadBankService.CHALLENGE_ID;
    }

    @Override
    public GameSummary summary(UUID userId, LocalDate today) {
        List<Object[]> best = runRepository.bestStarsByDay(userId);
        int stars = best.stream().mapToInt(row -> ((Number) row[1]).intValue()).sum();
        int won = (int) best.stream().filter(row -> ((Number) row[1]).intValue() > 0).count();
        return new GameSummary(stars, won, 0, 0, 0, new HashSet<>(runRepository.findPlayDaysSince(userId, today.minusDays(HISTORY_DAYS))));
    }
}
