package com.spotit.api.log.controller;

final class LogControllerSwagger {
    private LogControllerSwagger() {
    }

    static final String TEMPLATE_SUMMARY = "Get log template";
    static final String TEMPLATE_DESCRIPTION = "Returns the flow, mood, and symptom options the log entry UI renders. "
            + "Call this first; the ids returned here are exactly the values PUT /logs/{date} accepts for flow, mood, and symptoms.";
    static final String TEMPLATE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "flow": [
                  { "id": "spotting", "label": "Spotting" },
                  { "id": "light", "label": "Light" },
                  { "id": "medium", "label": "Medium" },
                  { "id": "heavy", "label": "Heavy" }
                ],
                "mood": [
                  { "id": "happy", "label": "Happy" },
                  { "id": "calm", "label": "Calm" },
                  { "id": "energetic", "label": "Energetic" },
                  { "id": "neutral", "label": "Neutral" },
                  { "id": "sad", "label": "Sad" },
                  { "id": "anxious", "label": "Anxious" },
                  { "id": "irritable", "label": "Irritable" },
                  { "id": "emotional", "label": "Emotional" }
                ],
                "symptoms": [
                  { "id": "headache", "label": "Headache" },
                  { "id": "dizziness", "label": "Dizziness" },
                  { "id": "cramps", "label": "Cramps" },
                  { "id": "backpain", "label": "Back pain" },
                  { "id": "fatigue", "label": "Fatigue" },
                  { "id": "jointpain", "label": "Joint pain" },
                  { "id": "nausea", "label": "Nausea" },
                  { "id": "bloating", "label": "Bloating" },
                  { "id": "tender", "label": "Tender breasts" },
                  { "id": "pelvicpain", "label": "Pelvic pain" },
                  { "id": "discharge", "label": "Discharge" },
                  { "id": "sweating", "label": "Night sweats" },
                  { "id": "acne", "label": "Acne" },
                  { "id": "moodswings", "label": "Mood swings" },
                  { "id": "insomnia", "label": "Insomnia" },
                  { "id": "anxiety", "label": "Anxiety" }
                ],
                "basePoints": 80
              }
            }
            """;

    static final String SAVE_LOG_SUMMARY = "Save a day's log";
    static final String SAVE_LOG_DESCRIPTION = "Creates or overwrites the log entry for the given date. New same-day entries earn SpotPoints and update the logging streak.";
    static final String SAVE_LOG_REQUEST_EXAMPLE = """
            {
              "flow": "medium",
              "mood": "calm",
              "symptoms": ["cramps", "fatigue"],
              "notes": "Felt okay today.",
              "intimate": false
            }
            """;
    static final String SAVE_LOG_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "date": "2026-07-10",
                "flow": "medium",
                "mood": "calm",
                "symptoms": ["cramps", "fatigue"],
                "notes": "Felt okay today.",
                "intimate": false,
                "pointsAwarded": 80,
                "newBalance": 480,
                "streak": 4,
                "isNewEntry": true
              }
            }
            """;

    static final String LOG_PERIOD_SUMMARY = "Log a period range";
    static final String LOG_PERIOD_DESCRIPTION = "Marks startDate..endDate (inclusive) as a period: sets flow on every day in the range (full "
            + "mood/symptoms/notes/intimate detail applies only to detailDate, which defaults to startDate if omitted — pass it explicitly when the "
            + "caller is describing a different day in the range, e.g. logging from the last day of a period), and resyncs the account's "
            + "lastPeriodDate so cycle/fertile/ovulation predictions reflect this period going forward. If this edit moves or shrinks a previously "
            + "logged period, days that fall outside the new range but were part of the old one have their stale flow cleared (or the row deleted "
            + "outright if it had no other data) — clearedEntries reports each one so the caller can fix its own cached day-log state to match. "
            + "Pass replaceStart (any day of an already logged period, or the account's lastPeriodDate) when the caller is moving that period "
            + "rather than adding a new one: every flow day of that period outside the new range is cleared too, and lastPeriodDate follows the "
            + "moved period.";
    static final String LOG_PERIOD_REQUEST_EXAMPLE = """
            {
              "startDate": "2026-07-10",
              "endDate": "2026-07-15",
              "detailDate": "2026-07-10",
              "flow": "medium",
              "mood": "calm",
              "symptoms": ["cramps", "fatigue"],
              "notes": "Felt okay today.",
              "intimate": false,
              "replaceStart": null
            }
            """;
    static final String LOG_PERIOD_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "startDate": "2026-07-10",
                "endDate": "2026-07-15",
                "flow": "medium",
                "lastPeriodDate": "2026-07-10",
                "cycleLength": 28,
                "periodLength": 6,
                "startDayEntry": {
                  "date": "2026-07-10",
                  "flow": "medium",
                  "mood": "calm",
                  "symptoms": ["cramps", "fatigue"],
                  "notes": "Felt okay today.",
                  "intimate": false
                },
                "pointsAwarded": 10,
                "newBalance": 410,
                "streak": 4,
                "clearedEntries": []
              }
            }
            """;
    static final String LOG_PERIOD_422_EXAMPLE = """
            {
              "code": 422,
              "message": "'startDate' must be before or equal to 'endDate'.",
              "data": { "errorCode": "validation_error" }
            }
            """;

    static final String GET_LOG_SUMMARY = "Get a day's log";
    static final String GET_LOG_DESCRIPTION = "Returns the log entry for the given date, or an empty entry if nothing was logged that day.";
    static final String GET_LOG_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "date": "2026-07-10",
                "flow": "medium",
                "mood": "calm",
                "symptoms": ["cramps", "fatigue"],
                "notes": "Felt okay today.",
                "intimate": false
              }
            }
            """;

    static final String GET_LOGS_RANGE_SUMMARY = "Get logs in a date range";
    static final String GET_LOGS_RANGE_DESCRIPTION = "Returns all log entries between two dates (inclusive), keyed by ISO date string.";
    static final String GET_LOGS_RANGE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "logs": {
                  "2026-07-09": {
                    "date": "2026-07-09",
                    "flow": "light",
                    "mood": "happy",
                    "symptoms": [],
                    "notes": null,
                    "intimate": false
                  },
                  "2026-07-10": {
                    "date": "2026-07-10",
                    "flow": "medium",
                    "mood": "calm",
                    "symptoms": ["cramps", "fatigue"],
                    "notes": "Felt okay today.",
                    "intimate": false
                  }
                }
              }
            }
            """;
    static final String GET_LOGS_RANGE_422_EXAMPLE = """
            {
              "code": 422,
              "message": "'from' must be before or equal to 'to'.",
              "data": { "errorCode": "validation_error" }
            }
            """;

    static final String DELETE_LOG_SUMMARY = "Delete a day's log";
    static final String DELETE_LOG_DESCRIPTION = "Deletes the log entry for the given date, if any.";
    static final String DELETE_LOG_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Entry deleted.",
              "data": null
            }
            """;

    static final String REMOVE_PERIOD_SUMMARY = "Remove a logged period";
    static final String REMOVE_PERIOD_DESCRIPTION = "Clears the flow from every day of the logged period that contains the given date (days "
            + "with no other data are deleted) and, when that period was the account's lastPeriodDate, moves lastPeriodDate to the start of the "
            + "latest remaining period, or null if none is left. clearedEntries reports each changed day.";
    static final String REMOVE_PERIOD_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "lastPeriodDate": "2026-09-12",
                "trackingStartDate": "2026-09-01",
                "clearedEntries": [
                  { "date": "2026-10-07", "flow": null, "mood": null, "symptoms": [], "notes": null, "intimate": false }
                ]
              }
            }
            """;
    static final String REMOVE_PERIOD_404_EXAMPLE = """
            {
              "code": 404,
              "message": "No logged period on that day.",
              "data": { "errorCode": "not_found" }
            }
            """;
}
