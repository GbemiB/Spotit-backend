package com.spotit.api.rewards.dto;

public record DailyClaimResponse(int pointsAwarded, long newBalance, boolean alreadyClaimedToday) {
}
