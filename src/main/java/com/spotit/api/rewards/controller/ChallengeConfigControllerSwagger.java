package com.spotit.api.rewards.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link ChallengeConfigController}, kept out of the controller so endpoint
 * methods stay focused on request handling. Mirrors the companion-class
 * convention used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class ChallengeConfigControllerSwagger {

    private ChallengeConfigControllerSwagger() {
    }

    static final String LIST_SUMMARY = "List challenge definitions";
    static final String LIST_DESCRIPTION = "Returns every weekly-challenge definition in the catalog, for admin management.";
    static final String LIST_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                { "id": "log_5_days", "title": "Log 5 days this week", "reward": 150, "total": 5, "type": "WEEKLY_LOG" },
                { "id": "streak_7", "title": "Keep a 7-day streak", "reward": 200, "total": 7, "type": "STATIC" }
              ]
            }
            """;

    static final String GET_SUMMARY = "Get a challenge definition";
    static final String GET_DESCRIPTION = "Returns a single weekly-challenge definition by id, for admin management.";
    static final String GET_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": { "id": "log_5_days", "title": "Log 5 days this week", "reward": 150, "total": 5, "type": "WEEKLY_LOG" }
            }
            """;

    static final String NOT_FOUND_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Challenge definition not found.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String CREATE_SUMMARY = "Create a challenge definition";
    static final String CREATE_DESCRIPTION = "Adds a new weekly-challenge definition to the catalog.";
    static final String CREATE_REQUEST_EXAMPLE = """
            {
              "id": "log_5_days",
              "title": "Log 5 days this week",
              "reward": 150,
              "total": 5,
              "type": "WEEKLY_LOG"
            }
            """;
    static final String CREATE_201_EXAMPLE = """
            {
              "code": 201,
              "message": "Created",
              "data": { "id": "log_5_days", "title": "Log 5 days this week", "reward": 150, "total": 5, "type": "WEEKLY_LOG" }
            }
            """;
    static final String CREATE_409_EXAMPLE = """
            {
              "code": 409,
              "message": "A challenge with id 'log_5_days' already exists.",
              "data": { "errorCode": "resource_already_exists" }
            }
            """;

    static final String UPDATE_SUMMARY = "Update a challenge definition";
    static final String UPDATE_DESCRIPTION = "Partially updates a challenge definition; only non-null fields are applied.";
    static final String UPDATE_REQUEST_EXAMPLE = """
            {
              "title": "Log 5 days this week",
              "reward": 175,
              "total": 5
            }
            """;
    static final String UPDATE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": { "id": "log_5_days", "title": "Log 5 days this week", "reward": 175, "total": 5, "type": "WEEKLY_LOG" }
            }
            """;

    static final String DELETE_SUMMARY = "Delete a challenge definition";
    static final String DELETE_DESCRIPTION = "Removes a weekly-challenge definition from the catalog.";
    static final String DELETE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Challenge definition deleted.",
              "data": null
            }
            """;
}
