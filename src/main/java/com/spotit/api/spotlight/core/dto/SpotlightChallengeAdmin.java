package com.spotit.api.spotlight.core.dto;

import com.spotit.api.spotlight.core.entity.SpotlightChallenge;

public record SpotlightChallengeAdmin(
        String id,
        String kind,
        String name,
        String blurb,
        String icon,
        String tone,
        Integer cost,
        Boolean costFrom,
        Integer minAge,
        String status,
        Integer sortOrder,
        Boolean active,
        Integer freePlays,
        String partnerName
) {
    public static SpotlightChallengeAdmin of(SpotlightChallenge c) {
        return new SpotlightChallengeAdmin(c.getId(), c.getKind(), c.getName(), c.getBlurb(), c.getIcon(), c.getTone(), c.getCost(),
                c.isCostFrom(), c.getMinAge(), c.getStatus(), c.getSortOrder(), c.isActive(), c.getFreePlays(),
                c.getPartnerName());
    }
}
