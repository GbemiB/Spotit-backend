package com.spotit.api.spotlight.giveday.dto;

import java.util.List;

public record GiveDayPlaceView(
        String id,
        String name,
        String kind,
        String teaser,
        String quote,
        String hostName,
        String hostLook,
        String who,
        int total,
        List<Integer> counts,
        List<GiveDayFact> facts,
        int x,
        int y,
        boolean forSelf,
        boolean locked
) {
}
