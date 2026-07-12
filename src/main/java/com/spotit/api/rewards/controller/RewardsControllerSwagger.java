package com.spotit.api.rewards.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link RewardsController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class RewardsControllerSwagger {

    private RewardsControllerSwagger() {
    }

    static final String SUMMARY_SUMMARY = "Get rewards summary";
    static final String SUMMARY_DESCRIPTION = "Returns the current user's points balance, level, streak, and progress toward the next level.";
    static final String SUMMARY_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "points": 480,
                "level": "bronze",
                "levelRange": { "lo": 0, "hi": 999 },
                "nextLevel": "silver",
                "pointsToNextLevel": 520,
                "streak": 4,
                "longestStreak": 12
              }
            }
            """;

    static final String DAILY_CLAIM_SUMMARY = "Claim daily bonus";
    static final String DAILY_CLAIM_DESCRIPTION = "Awards the daily check-in bonus if it hasn't already been claimed today; otherwise returns zero points with alreadyClaimedToday=true.";
    static final String DAILY_CLAIM_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "pointsAwarded": 50,
                "newBalance": 530,
                "alreadyClaimedToday": false
              }
            }
            """;

    static final String WATCH_AD_SUMMARY = "Claim rewarded-ad points";
    static final String WATCH_AD_DESCRIPTION = "Awards points for watching a rewarded ad, up to the configured daily limit.";
    static final String WATCH_AD_REQUEST_EXAMPLE = """
            {
              "adNetwork": "admob",
              "adUnitId": "ca-app-pub-xxx/yyy",
              "verificationToken": "srv-verif-token-example"
            }
            """;
    static final String WATCH_AD_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "pointsAwarded": 100,
                "newBalance": 630
              }
            }
            """;
    static final String WATCH_AD_429_EXAMPLE = """
            {
              "code": 429,
              "message": "You've reached today's rewarded-ad limit.",
              "data": { "errorCode": "daily_ad_limit_reached" }
            }
            """;

    static final String HISTORY_SUMMARY = "Get points history";
    static final String HISTORY_DESCRIPTION = "Returns a cursor-paginated feed of points-earning/spending events, most recent first.";
    static final String HISTORY_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "entries": [
                  { "icon": "📝", "label": "Logged flow, mood & symptoms", "delta": 80, "date": "2026-07-10" },
                  { "icon": "🎁", "label": "Daily check-in bonus", "delta": 50, "date": "2026-07-10" }
                ],
                "nextCursor": "MjA="
              }
            }
            """;

    static final String BADGES_SUMMARY = "Get badges";
    static final String BADGES_DESCRIPTION = "Returns every badge with its earned status for the current user, syncing any newly-earned badges first.";
    static final String BADGES_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                { "id": "first_flow", "name": "First Flow", "earned": true, "earnedAt": "2026-05-01T09:12:00Z" },
                { "id": "week_warrior", "name": "Week Warrior", "earned": false, "earnedAt": null }
              ]
            }
            """;

    static final String CHALLENGES_SUMMARY = "Get weekly challenges";
    static final String CHALLENGES_DESCRIPTION = "Returns every weekly-challenge definition with the current user's progress toward it.";
    static final String CHALLENGES_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                { "id": "log_5_days", "title": "Log 5 days this week", "reward": 150, "done": 3, "total": 5, "completed": false, "claimed": false },
                { "id": "streak_7", "title": "Keep a 7-day streak", "reward": 200, "done": 7, "total": 7, "completed": true, "claimed": false }
              ]
            }
            """;

    static final String CLAIM_CHALLENGE_SUMMARY = "Claim a challenge reward";
    static final String CLAIM_CHALLENGE_DESCRIPTION = "Claims the reward for a completed weekly challenge. Fails if the challenge isn't complete yet or was already claimed this week.";
    static final String CLAIM_CHALLENGE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "pointsAwarded": 200,
                "newBalance": 830
              }
            }
            """;
    static final String CLAIM_CHALLENGE_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Challenge not found.",
              "data": { "errorCode": "not_found" }
            }
            """;
    static final String CLAIM_CHALLENGE_409_EXAMPLE = """
            {
              "code": 409,
              "message": "This challenge isn't complete yet.",
              "data": { "errorCode": "not_yet_complete" }
            }
            """;
}
