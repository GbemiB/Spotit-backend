package com.spotit.api.spotlight.games.giveday.dto;

import com.spotit.api.spotlight.games.giveday.entity.GiveDayArea;

public record GiveDayAreaAdmin(String id, String name, String region, Integer x, Integer y, Integer sortOrder, Boolean active) {
    public static GiveDayAreaAdmin of(GiveDayArea a) {
        return new GiveDayAreaAdmin(a.getId(), a.getName(), a.getRegion(), a.getMapX(), a.getMapY(), a.getSortOrder(), a.isActive());
    }
}
