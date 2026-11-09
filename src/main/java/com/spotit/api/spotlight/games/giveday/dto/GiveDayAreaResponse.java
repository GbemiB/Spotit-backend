package com.spotit.api.spotlight.games.giveday.dto;

import java.util.List;

public record GiveDayAreaResponse(
        String areaId,
        String name,
        int level,
        boolean locked,
        int starsNeeded,
        boolean passNeeded,
        int passCost,
        int giftBudget,
        int giftMax,
        boolean extrasFree,
        List<GiveDayPlaceView> places
) {
}
