package com.spotit.api.spotlight.games.giveday.dto;

import java.util.List;

public record GiveDayTripsResponse(
        String placeId,
        int clock,
        int closes,
        int level,
        boolean levelOpen,
        int starsNeeded,
        boolean passNeeded,
        int passCost,
        int giftBudget,
        int giftMax,
        boolean extrasFree,
        List<GiveDayTripQuote> quotes
) {
}
