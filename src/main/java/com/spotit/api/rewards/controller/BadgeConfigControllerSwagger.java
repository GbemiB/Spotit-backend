package com.spotit.api.rewards.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link BadgeConfigController}, kept out of the controller so endpoint
 * methods stay focused on request handling. Mirrors the companion-class
 * convention used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class BadgeConfigControllerSwagger {

    private BadgeConfigControllerSwagger() {
    }

    static final String LIST_SUMMARY = "List badge definitions";
    static final String LIST_DESCRIPTION = "Returns every badge definition in the catalog, for admin management.";
    static final String LIST_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": [
                { "id": "first_flow", "name": "First Flow", "description": "Logged your first entry." },
                { "id": "week_warrior", "name": "Week Warrior", "description": "Kept a 7-day logging streak." }
              ]
            }
            """;

    static final String GET_SUMMARY = "Get a badge definition";
    static final String GET_DESCRIPTION = "Returns a single badge definition by id, for admin management.";
    static final String GET_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": { "id": "first_flow", "name": "First Flow", "description": "Logged your first entry." }
            }
            """;

    static final String NOT_FOUND_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Badge definition not found.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String CREATE_SUMMARY = "Create a badge definition";
    static final String CREATE_DESCRIPTION = "Adds a new badge to the catalog.";
    static final String CREATE_REQUEST_EXAMPLE = """
            {
              "id": "night_owl",
              "name": "Night Owl",
              "description": "Logged an entry after midnight."
            }
            """;
    static final String CREATE_201_EXAMPLE = """
            {
              "code": 201,
              "message": "Created",
              "data": { "id": "night_owl", "name": "Night Owl", "description": "Logged an entry after midnight." }
            }
            """;
    static final String CREATE_409_EXAMPLE = """
            {
              "code": 409,
              "message": "A badge with id 'night_owl' already exists.",
              "data": { "errorCode": "resource_already_exists" }
            }
            """;

    static final String UPDATE_SUMMARY = "Update a badge definition";
    static final String UPDATE_DESCRIPTION = "Partially updates a badge definition; only non-null fields are applied.";
    static final String UPDATE_REQUEST_EXAMPLE = """
            {
              "name": "Night Owl",
              "description": "Logged an entry between midnight and 4am."
            }
            """;
    static final String UPDATE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": { "id": "night_owl", "name": "Night Owl", "description": "Logged an entry between midnight and 4am." }
            }
            """;

    static final String DELETE_SUMMARY = "Delete a badge definition";
    static final String DELETE_DESCRIPTION = "Removes a badge definition from the catalog.";
    static final String DELETE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Badge definition deleted.",
              "data": null
            }
            """;
}
