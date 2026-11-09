package com.spotit.api.spotlight.games.giveday.dto;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayPlace;
import com.spotit.api.spotlight.games.giveday.service.GiveDayRules;

import java.util.List;

public record GiveDayPlaceAdmin(
        String id,
        String areaId,
        String name,
        String kind,
        String teaser,
        String quote,
        String hostName,
        String hostLook,
        String who,
        Integer total,
        String thanks,
        List<GiveDayFact> facts,
        Integer x,
        Integer y,
        Integer closes,
        Boolean forSelf,
        Integer minAge,
        Integer sortOrder,
        Boolean active
) {
    public static GiveDayPlaceAdmin of(GiveDayPlace p) {
        return new GiveDayPlaceAdmin(p.getId(), p.getAreaId(), p.getName(), p.getKind(), p.getTeaser(), p.getQuote(), p.getHostName(), p.getHostLook(),
                p.getWho(), p.getTotal(), p.getThanks(), GiveDayRules.decodeFacts(p.getFacts()), p.getMapX(), p.getMapY(), p.getCloses(), p.isForSelf(),
                p.getMinAge(), p.getSortOrder(), p.isActive());
    }
}
