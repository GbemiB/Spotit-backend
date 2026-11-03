package com.spotit.api.cycle.controller;

final class CycleControllerSwagger {
    private CycleControllerSwagger() {
    }

    static final String CURRENT_SUMMARY = "Get current cycle status";
    static final String CURRENT_DESCRIPTION = "Returns the current cycle day, phase, next predicted period date and prediction confidence. "
            + "status is tracking within the confirmed cycle, due on a predicted start that has not been logged, and late after it; "
            + "late counts until a full cycle has passed, then the next predicted start becomes due (missedCycles goes up by one).";
    static final String CURRENT_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "cycleDay": 14,
                "phase": "ovulation",
                "nextPeriodDate": "2026-07-24",
                "daysUntilNextPeriod": 14,
                "confidence": "high",
                "status": "tracking",
                "daysLate": 0,
                "expectedDate": "2026-07-24",
                "missedCycles": 0,
                "trackingStartDate": "2026-07-01"
              }
            }
            """;

    static final String CALENDAR_SUMMARY = "Get calendar month";
    static final String CALENDAR_DESCRIPTION = "Returns the cycle phase for every day in the given month. Nothing is shown before the month of the "
            + "user's last period. Logged and confirmed days have predicted false; days from today on are predictions (predicted true). "
            + "marker is expected on the predicted start currently due or late, and missed on one that passed a full cycle without a log.";
    static final String CALENDAR_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "year": 2026,
                "month": 7,
                "days": [
                  { "date": "2026-07-01", "phase": "period", "predicted": false, "marker": null },
                  { "date": "2026-07-29", "phase": "period", "predicted": true, "marker": "expected" }
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
