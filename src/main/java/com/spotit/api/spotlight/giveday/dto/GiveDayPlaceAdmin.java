package com.spotit.api.spotlight.giveday.dto;

import com.spotit.api.spotlight.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.giveday.service.GiveDayRules;

import java.util.List;

public record GiveDayPlaceAdmin(
        String id,
        String name,
        String kind,
        String teaser,
        String quote,
        String hostName,
        String hostLook,
        String who,
        Integer total,
        List<Integer> counts,
        String thanks,
        List<GiveDayFact> facts,
        Integer x,
        Integer y,
        Boolean forSelf,
        Integer minAge,
        Integer sortOrder,
        Boolean active
) {
    public static GiveDayPlaceAdmin of(GiveDayPlace p) {
        return new GiveDayPlaceAdmin(p.getId(), p.getName(), p.getKind(), p.getTeaser(), p.getQuote(), p.getHostName(), p.getHostLook(), p.getWho(),
                p.getTotal(), GiveDayRules.decodeCounts(p.getCounts()), p.getThanks(), GiveDayRules.decodeFacts(p.getFacts()), p.getMapX(), p.getMapY(),
                p.isForSelf(), p.getMinAge(), p.getSortOrder(), p.isActive());
    }
}
