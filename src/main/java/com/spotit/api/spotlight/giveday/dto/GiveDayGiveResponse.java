package com.spotit.api.spotlight.giveday.dto;

import java.util.List;

public record GiveDayGiveResponse(
        String placeId,
        int stars,
        String title,
        String thanks,
        List<GiveDayReview> review,
        List<GiveDayReview> missed,
        int people,
        int giftCost,
        int giftBudget,
        int giftPaid,
        boolean extrasFree,
        int spSpent,
        long balance,
        int helped,
        int clock,
        int playerStars,
        int level,
        int refunded,
        boolean canRepack
) {
}
