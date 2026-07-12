package com.spotit.api.insight.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link InsightController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class InsightControllerSwagger {

    private InsightControllerSwagger() {
    }

    static final String TRENDS_SUMMARY = "Get cycle trends";
    static final String TRENDS_DESCRIPTION = "Returns recent cycle lengths and averages computed from logged period episodes, over the last N cycles (default 6).";
    static final String TRENDS_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "cycleLengths": [28, 29, 27],
                "avgCycleLength": 28,
                "avgPeriodLength": 5,
                "variationDays": 1
              }
            }
            """;

    static final String WEEKLY_DIGEST_SUMMARY = "Get weekly digest";
    static final String WEEKLY_DIGEST_DESCRIPTION = "Returns a summary of logging activity and the most common mood over the last 7 days.";
    static final String WEEKLY_DIGEST_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "loggedCount": 5,
                "topMood": "CALM",
                "rangeStart": "2026-07-04",
                "rangeEnd": "2026-07-10"
              }
            }
            """;

    static final String REGULARITY_SUMMARY = "Get regularity flags";
    static final String REGULARITY_DESCRIPTION = "Returns a regularity status and human-readable flags describing whether recent cycles/periods look unusual. Not medical advice.";
    static final String REGULARITY_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "status": "regular",
                "flags": [],
                "disclaimer": "This is not medical advice."
              }
            }
            """;
}
