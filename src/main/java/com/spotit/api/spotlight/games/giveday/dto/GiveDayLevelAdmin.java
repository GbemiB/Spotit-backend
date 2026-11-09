package com.spotit.api.spotlight.games.giveday.dto;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayLevel;

public record GiveDayLevelAdmin(Integer number, String name, Integer radiusKm, Integer starsNeeded, Integer passCost, Integer giftBudget, Integer giftMax, Boolean extrasFree,
        Boolean active) {
    public static GiveDayLevelAdmin of(GiveDayLevel l) {
        return new GiveDayLevelAdmin(l.getNumber(), l.getName(), l.getRadiusKm(), l.getStarsNeeded(), l.getPassCost(), l.getGiftBudget(), l.getGiftMax(), l.isExtrasFree(),
                l.isActive());
    }
}
