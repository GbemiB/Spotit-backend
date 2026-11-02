package com.spotit.api.spotlight.giveday.dto;

import java.util.List;
import java.util.UUID;

public record GiveDayStartResponse(
        UUID runId,
        long balance,
        int cost,
        int coins,
        List<GiveDayPlaceView> places,
        List<GiveDayProductView> products
) {
}
