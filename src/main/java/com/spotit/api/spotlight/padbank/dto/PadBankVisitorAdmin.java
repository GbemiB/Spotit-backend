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
        List<String> packs,
        String route,
        String thanks,
        String routeWhy,
        String groupWhy,
        Map<String, String> wrongWhy,
        Boolean active
) {
    public static PadBankVisitorAdmin of(PadBankVisitor v) {
        return new PadBankVisitorAdmin(v.getId(), v.getDay(), v.getSortOrder(), v.getWho(), v.getLook(), PadBankRules.decodeClues(v.getClues()),
                PadBankRules.decodePacks(v.getPacks()), v.getRoute(), v.getThanks(), v.getRouteWhy(), v.getGroupWhy(), PadBankRules.decodeMap(v.getWrongWhy()), v.isActive());
    }
}
