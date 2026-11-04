package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.core.service.SpotlightGame;
import com.spotit.api.spotlight.giveday.entity.GiveDayLevel;
import com.spotit.api.spotlight.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.giveday.repository.GiveDayRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GiveDaySpotlightGame implements SpotlightGame {
    static final int HISTORY_DAYS = 400;
    private final GiveDayRunRepository runRepository;
    private final GiveDayLevelRepository levelRepository;

    @Override
    public String challengeId() {
        return GiveDayService.CHALLENGE_ID;
    }

    @Override
    public int freeUpTo() {
        int free = 0;
        for (GiveDayLevel level : levelRepository.findByActiveTrueOrderByNumberAsc()) {
            if (!level.isExtrasFree() || level.getPassCost() > 0) {
                break;
            }
            free = level.getNumber();
        }
        return free;
    }

    @Override
    public GameSummary summary(UUID userId, LocalDate today) {
        List<GiveDayRun> runs = runRepository.findByUserId(userId);
        Map<String, Integer> best = new HashMap<>();
        runs.forEach(r -> GiveDayRules.decodeVisits(r.getVisits()).forEach((place, stars) -> best.merge(place, stars, Math::max)));
        int stars = best.values().stream().mapToInt(Integer::intValue).sum();
        int won = (int) best.values().stream().filter(s -> s > 0).count();
        LocalDate since = today.minusDays(HISTORY_DAYS);
        Set<LocalDate> days = runs.stream().map(GiveDayRun::getPlayedOn).filter(d -> !d.isBefore(since)).collect(Collectors.toSet());
        return new GameSummary(stars, won, 0, 0, 0, days, runs.size());
    }
}
