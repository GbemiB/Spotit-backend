package com.spotit.api.rewards.dto;

public record RewardsSummaryResponse(
        long points,
        String level,
        LevelRange levelRange,
        String nextLevel,
        Long pointsToNextLevel,
        int streak,
        int longestStreak
) {
    public record LevelRange(long lo, long hi) {
    }
}
