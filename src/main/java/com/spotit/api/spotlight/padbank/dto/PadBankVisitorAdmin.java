package com.spotit.api.spotlight.padbank.dto;

import com.spotit.api.spotlight.padbank.entity.PadBankVisitor;
import com.spotit.api.spotlight.padbank.service.PadBankRules;

import java.util.List;
import java.util.Map;

public record PadBankVisitorAdmin(
        String id,
        Integer day,
        Integer sortOrder,
        String who,
        String look,
        List<PadBankClue> clues,
        String pack,
        String route,
        String thanks,
        String routeWhy,
        Map<String, String> wrongWhy,
        Boolean active
) {
    public static PadBankVisitorAdmin of(PadBankVisitor v) {
        return new PadBankVisitorAdmin(v.getId(), v.getDay(), v.getSortOrder(), v.getWho(), v.getLook(), PadBankRules.decodeClues(v.getClues()),
                v.getPack(), v.getRoute(), v.getThanks(), v.getRouteWhy(), PadBankRules.decodeMap(v.getWrongWhy()), v.isActive());
    }
}
