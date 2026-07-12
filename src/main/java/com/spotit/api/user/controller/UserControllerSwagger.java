package com.spotit.api.user.controller;

/**
 * Swagger/OpenAPI summaries, descriptions, and example payloads for
 * {@link UserController}, kept out of the controller so endpoint methods
 * stay focused on request handling. Mirrors the companion-class convention
 * used by Apache Fineract's {@code *ApiResourceSwagger} classes.
 */
final class UserControllerSwagger {

    private UserControllerSwagger() {
    }

    static final String GET_PROFILE_SUMMARY = "Get profile";
    static final String GET_PROFILE_DESCRIPTION = "Returns the current user's profile.";
    static final String GET_PROFILE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "firstName": "Amara",
                "lastName": "Okafor",
                "email": "amara@example.com",
                "dob": "1998-04-12",
                "goal": "track",
                "cycleLength": 28,
                "periodLength": 5,
                "lastPeriodDate": "2026-06-28",
                "themePref": "system",
                "onboarded": true,
                "isPremium": false
              }
            }
            """;

    static final String UPDATE_PROFILE_SUMMARY = "Update profile";
    static final String UPDATE_PROFILE_DESCRIPTION = "Partially updates the current user's profile; only non-null fields are applied. cycleLength must be 21-45 and periodLength must be 2-10.";
    static final String UPDATE_PROFILE_REQUEST_EXAMPLE = """
            {
              "firstName": "Amara",
              "lastName": "Okafor",
              "cycleLength": 30,
              "periodLength": 5,
              "themePref": "dark"
            }
            """;
    static final String UPDATE_PROFILE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "firstName": "Amara",
                "lastName": "Okafor",
                "email": "amara@example.com",
                "dob": "1998-04-12",
                "goal": "track",
                "cycleLength": 30,
                "periodLength": 5,
                "lastPeriodDate": "2026-06-28",
                "themePref": "dark",
                "onboarded": true,
                "isPremium": false
              }
            }
            """;
    static final String UPDATE_PROFILE_422_EXAMPLE = """
            {
              "code": 422,
              "message": "cycleLength must be between 21 and 45.",
              "data": { "errorCode": "cycle_length_out_of_range" }
            }
            """;

    static final String GET_NOTIFICATIONS_SUMMARY = "Get notification preferences";
    static final String GET_NOTIFICATIONS_DESCRIPTION = "Returns the current user's notification toggle settings.";
    static final String GET_NOTIFICATIONS_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "period": true,
                "ovulation": true,
                "dailyLog": true,
                "digest": false
              }
            }
            """;

    static final String UPDATE_NOTIFICATIONS_SUMMARY = "Update notification preferences";
    static final String UPDATE_NOTIFICATIONS_DESCRIPTION = "Partially updates notification toggles; only non-null fields are applied.";
    static final String UPDATE_NOTIFICATIONS_REQUEST_EXAMPLE = """
            {
              "period": true,
              "ovulation": false,
              "dailyLog": true,
              "digest": true
            }
            """;
    static final String UPDATE_NOTIFICATIONS_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "period": true,
                "ovulation": false,
                "dailyLog": true,
                "digest": true
              }
            }
            """;

    static final String EXPORT_SUMMARY = "Request a data export";
    static final String EXPORT_DESCRIPTION = "Creates an export job for the current user's data. Export data is synchronously ready and can be downloaded immediately via the returned jobId.";
    static final String EXPORT_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "jobId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                "status": "ready"
              }
            }
            """;

    static final String DOWNLOAD_EXPORT_SUMMARY = "Download export data";
    static final String DOWNLOAD_EXPORT_DESCRIPTION = "Returns the exported profile, logs, and points history for a previously-created export job.";
    static final String DOWNLOAD_EXPORT_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "profile": {
                  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                  "firstName": "Amara",
                  "lastName": "Okafor",
                  "email": "amara@example.com",
                  "dob": "1998-04-12",
                  "goal": "track",
                  "cycleLength": 28,
                  "periodLength": 5,
                  "lastPeriodDate": "2026-06-28",
                  "themePref": "system",
                  "onboarded": true,
                  "isPremium": false
                },
                "logs": [
                  { "date": "2026-07-10", "flow": "medium", "mood": "calm", "symptoms": ["cramps"], "notes": "" }
                ],
                "pointsHistory": [
                  { "label": "Logged flow, mood & symptoms", "delta": 80, "date": "2026-07-10" }
                ]
              }
            }
            """;
    static final String DOWNLOAD_EXPORT_404_EXAMPLE = """
            {
              "code": 404,
              "message": "Export job not found.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String RESET_SUMMARY = "Reset all data";
    static final String RESET_DESCRIPTION = "Deletes the current user's logs, points history, badges, and challenge progress, and resets profile settings to defaults. Does not delete the account itself.";
    static final String RESET_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "message": "All data has been reset.",
                "resetAt": "2026-07-10T09:00:00Z"
              }
            }
            """;
}
