package com.spotit.api.spotlight.giveday.dto;

import com.spotit.api.spotlight.giveday.entity.GiveDayProduct;

public record GiveDayProductAdmin(String id, String name, Integer cost, Boolean absorbent, Integer sortOrder, Boolean active) {
    public static GiveDayProductAdmin of(GiveDayProduct p) {
        return new GiveDayProductAdmin(p.getId(), p.getName(), p.getCost(), p.isAbsorbent(), p.getSortOrder(), p.isActive());
    }
}
