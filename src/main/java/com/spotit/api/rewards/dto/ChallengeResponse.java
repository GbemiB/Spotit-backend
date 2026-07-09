package com.spotit.api.rewards.dto;

public record ChallengeResponse(String id, String title, int reward, int done, int total, boolean completed, boolean claimed) {
}
