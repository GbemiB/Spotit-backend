package com.spotit.api.spotlight.core.dto;

public record ChallengeCard(
        String id,
        String kind,
        String name,
        String blurb,
        String icon,
        String tone,
        Integer cost,
        boolean costFrom,
        int minAge,
        String status,
        boolean locked,
        int freePlaysLeft,
        int stars,
        long plays,
        int freeUpTo
) {
}
