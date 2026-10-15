package com.spotit.api.reminder.controller;

final class NotificationControllerSwagger {
    private NotificationControllerSwagger() {
    }

    static final String INBOX_SUMMARY = "List notifications";
    static final String INBOX_DESCRIPTION = "The reminders sent to the current user, newest first (up to 50), with the number still unread.";
    static final String INBOX_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "items": [
                  {
                    "id": "3f2c1c0e-8a55-4d0b-9c58-0b1f5c1f7a11",
                    "type": "period_upcoming",
                    "title": "Period reminder",
                    "body": "Your period is predicted to start in 3 days.",
                    "screen": "home",
                    "sentAt": "2026-10-10T08:00:04Z",
                    "read": false
                  }
                ],
                "unreadCount": 1
              }
            }
            """;

    static final String READ_SUMMARY = "Mark notifications as read";
    static final String READ_DESCRIPTION = "Marks every notification of the current user as read.";
    static final String READ_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Notifications marked as read.",
              "data": null
            }
            """;
}
