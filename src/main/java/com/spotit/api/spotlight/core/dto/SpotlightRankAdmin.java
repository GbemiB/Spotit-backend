package com.spotit.api.spotlight.core.dto;

import com.spotit.api.spotlight.core.entity.SpotlightRank;

public record SpotlightRankAdmin(String id, String name, Integer minStars, Boolean active) {
    public static SpotlightRankAdmin of(SpotlightRank r) {
        return new SpotlightRankAdmin(r.getId(), r.getName(), r.getMinStars(), r.isActive());
    }
}
