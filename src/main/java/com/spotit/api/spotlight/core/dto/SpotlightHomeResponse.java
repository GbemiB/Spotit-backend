package com.spotit.api.spotlight.core.dto;

import java.util.List;

public record SpotlightHomeResponse(
        long balance,
        SpotlightStats stats,
        List<ChallengeCard> challenges,
        SpotlightInvite invite,
        List<RankStep> ranks
) {
}
