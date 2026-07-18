package com.spotit.api.log.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link LogController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
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
}
