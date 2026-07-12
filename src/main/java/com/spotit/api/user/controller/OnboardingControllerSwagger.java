package com.spotit.api.user.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link OnboardingController}, kept out of the controller so endpoint
 * methods stay focused on request handling. Mirrors the companion-class
 * convention used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class OnboardingControllerSwagger {

    private OnboardingControllerSwagger() {
    }

    static final String TEMPLATE_SUMMARY = "Get onboarding template";
    static final String TEMPLATE_DESCRIPTION = "Returns the goal options the onboarding UI renders on its goals step. "
            + "Call this first; the ids returned here are exactly the values the /complete endpoint accepts for goal.";
    static final String TEMPLATE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "goals": [
                  { "id": "track", "label": "Track my cycle", "description": "Understand your body and patterns" },
                  { "id": "conceive", "label": "Try to conceive", "description": "Identify your fertile window" },
                  { "id": "avoid", "label": "Avoid pregnancy", "description": "Natural family planning support" },
                  { "id": "curious", "label": "Just curious", "description": "Explore what Spot it offers" }
                ]
              }
            }
            """;

    static final String COMPLETE_SUMMARY = "Complete onboarding";
    static final String COMPLETE_DESCRIPTION = "Records date of birth, last period date, tracking goal, and optional cycle/period length overrides, and marks the user as onboarded.";
    static final String COMPLETE_REQUEST_EXAMPLE = """
            {
              "dob": "1998-04-12",
              "lastPeriodDate": "2026-06-28",
              "goal": "track",
              "cycleLength": 30,
              "periodLength": 6
            }
            """;
    static final String COMPLETE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "onboarded": true,
                "cycleLength": 30,
                "periodLength": 6,
                "goal": "track"
              }
            }
            """;
}
