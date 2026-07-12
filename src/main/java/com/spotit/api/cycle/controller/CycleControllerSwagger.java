package com.spotit.api.cycle.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link CycleController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class CycleControllerSwagger {

    private CycleControllerSwagger() {
    }

    static final String CURRENT_SUMMARY = "Get current cycle status";
    static final String CURRENT_DESCRIPTION = "Returns the current cycle day, phase, next predicted period date, and prediction confidence for the current user.";
    static final String CURRENT_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "cycleDay": 14,
                "phase": "OVULATION",
                "nextPeriodDate": "2026-07-24",
                "daysUntilNextPeriod": 14,
                "confidence": "high"
              }
            }
            """;

    static final String CALENDAR_SUMMARY = "Get calendar month";
    static final String CALENDAR_DESCRIPTION = "Returns the predicted cycle phase for every day in the given month.";
    static final String CALENDAR_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "year": 2026,
                "month": 7,
                "days": [
                  { "date": "2026-07-01", "phase": "MENSTRUAL" },
                  { "date": "2026-07-02", "phase": "MENSTRUAL" }
                ]
              }
            }
            """;
    static final String CALENDAR_422_EXAMPLE = """
            {
              "code": 422,
              "message": "month must be between 1 and 12.",
              "data": { "errorCode": "validation_error" }
            }
            """;
}
