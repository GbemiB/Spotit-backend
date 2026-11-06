package com.spotit.api.spotlight.games.giveday.dto;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayTravel;

public record GiveDayTravelAdmin(String id, String label, Integer cost, Integer unitsPerMinute, Integer sortOrder, Boolean active) {
    public static GiveDayTravelAdmin of(GiveDayTravel t) {
        return new GiveDayTravelAdmin(t.getId(), t.getLabel(), t.getCost(), t.getUnitsPerMinute(), t.getSortOrder(), t.isActive());
    }
}
