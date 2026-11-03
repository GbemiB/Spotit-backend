package com.spotit.api.spotlight.giveday.dto;

import com.spotit.api.spotlight.giveday.entity.GiveDayNeed;

public record GiveDayNeedAdmin(String placeId, String productId, String rating, String why) {
    public static GiveDayNeedAdmin of(GiveDayNeed n) {
        return new GiveDayNeedAdmin(n.getPlaceId(), n.getProductId(), n.getRating(), n.getWhy());
    }
}
