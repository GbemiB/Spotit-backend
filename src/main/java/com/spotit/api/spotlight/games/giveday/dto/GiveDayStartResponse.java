package com.spotit.api.spotlight.games.giveday.dto;

import java.util.List;
import java.util.UUID;

public record GiveDayStartResponse(
        UUID runId,
        long balance,
        int cost,
        int clock,
        int dayEnd,
        int visitMinutes,
        int unitsPerKm,
        String home,
        int x,
        int y,
        int stars,
        int level,
        List<GiveDayLevelView> levels,
        List<GiveDayTravelView> travel,
        List<GiveDayAreaView> areas,
        List<GiveDayProductView> products
) {
}
