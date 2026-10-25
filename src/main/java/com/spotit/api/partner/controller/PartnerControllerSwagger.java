package com.spotit.api.partner.controller;

final class PartnerControllerSwagger {
    private PartnerControllerSwagger() {
    }

    static final String INVITE_SUMMARY = "Invite a partner";
    static final String INVITE_DESCRIPTION = "Emails a 6-character invite code to the partner's address. The code works only for that email and "
            + "expires after 48 hours. A new invite replaces any pending one. Only an onboarded owner account without a linked partner can invite; "
            + "at most 5 invites or resends per 24 hours.";
    static final String INVITE_REQUEST_EXAMPLE = """
            {
              "email": "tunde@example.com"
            }
            """;
    static final String STATUS_PENDING_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "role": "owner",
                "state": "pending",
                "inviteEmail": "tunde@example.com",
                "inviteExpiresAt": "2026-10-24T09:00:00Z",
                "partnerFirstName": null,
                "ownerFirstName": null,
                "nickname": null,
                "partnerReminder": null,
                "sharedSince": null
              }
            }
            """;
    static final String INVITE_409_EXAMPLE = """
            {
              "code": 409,
              "message": "You already share with a partner. Stop sharing first.",
              "data": { "errorCode": "partner_already_linked" }
            }
            """;
    static final String INVITE_429_EXAMPLE = """
            {
              "code": 429,
              "message": "You've sent 5 invites in the last 24 hours. Please try again later.",
              "data": { "errorCode": "invite_limit_reached" }
            }
            """;

    static final String RESEND_SUMMARY = "Resend the partner invite";
    static final String RESEND_DESCRIPTION = "Sends a new code with a fresh 48 hours to the pending invite's email. The old code stops working.";

    static final String CANCEL_SUMMARY = "Cancel the partner invite";
    static final String CANCEL_DESCRIPTION = "Cancels the pending invite so its code no longer works.";
    static final String CANCEL_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Invite cancelled.",
              "data": null
            }
            """;

    static final String ACCEPT_SUMMARY = "Accept a partner invite";
    static final String ACCEPT_DESCRIPTION = "Links the signed-in account to the owner who invited its email. The account becomes a read-only "
            + "partner account and skips cycle onboarding. Five wrong codes cancel the invite.";
    static final String ACCEPT_REQUEST_EXAMPLE = """
            {
              "code": "K7P2QX",
              "nickname": "Amaka"
            }
            """;
    static final String STATUS_PARTNER_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "role": "partner",
                "state": "active",
                "inviteEmail": null,
                "inviteExpiresAt": null,
                "partnerFirstName": null,
                "ownerFirstName": "Amaka",
                "nickname": "Amaka",
                "partnerReminder": true,
                "sharedSince": "2026-10-23T09:00:00Z"
              }
            }
            """;
    static final String ACCEPT_400_EXAMPLE = """
            {
              "code": 400,
              "message": "That invite code isn't right. Check the email and try again.",
              "data": { "errorCode": "invalid_code" }
            }
            """;
    static final String ACCEPT_410_EXAMPLE = """
            {
              "code": 410,
              "message": "This invite has expired. Ask your partner to send a new one.",
              "data": { "errorCode": "invite_expired" }
            }
            """;

    static final String STATUS_SUMMARY = "Partner sharing status";
    static final String STATUS_DESCRIPTION = "Owner: none, pending, expired or active. Partner: active or ended. "
            + "A new account that has not onboarded and has a valid invite for its email gets state invited with the inviter's first name.";

    static final String SETTINGS_SUMMARY = "Update partner sharing settings";
    static final String SETTINGS_DESCRIPTION = "Partner can set nickname and partnerReminder. Fields left out are unchanged.";
    static final String SETTINGS_REQUEST_EXAMPLE = """
            {
              "nickname": "Amaka",
              "partnerReminder": true,
                "sharedSince": "2026-10-23T09:00:00Z"
            }
            """;

    static final String UNLINK_SUMMARY = "Stop sharing";
    static final String UNLINK_DESCRIPTION = "Ends the active link. Either the owner or the partner can do this.";
    static final String UNLINK_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Sharing stopped.",
              "data": null
            }
            """;

    static final String PROFILE_SUMMARY = "Partner: her name and cycle settings";
    static final String PROFILE_DESCRIPTION = "For a linked partner only. The owner's first name, the name he saved for her and her cycle settings, "
            + "so his app shows her Home, Calendar and Insights. Her daily data comes from the normal /cycle, /logs and /insights "
            + "endpoints, which answer with her data for a linked partner.";
    static final String PROFILE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "firstName": "Amaka",
                "nickname": "Amaka",
                "cycleLength": 28,
                "periodLength": 5,
                "lastPeriodDate": "2026-10-06",
                "trackingStartDate": "2026-09-01"
              }
            }
            """;

    static final String CHECK_IN_GET_SUMMARY = "Partner: today's check-in";
    static final String CHECK_IN_GET_DESCRIPTION = "The ways he can say he supported her, whether he has checked in today, the reward and his streak.";
    static final String CHECK_IN_POST_SUMMARY = "Partner: check in for today";
    static final String CHECK_IN_POST_DESCRIPTION = "Records how he supported her today and awards his SpotPoints, once a day in his time zone. "
            + "Builds his own streak. Needs an active link.";
    static final String CHECK_IN_REQUEST_EXAMPLE = """
            {
              "supports": ["checked_in", "snacks"],
              "note": "Proud of you today."
            }
            """;
    static final String CHECK_IN_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "options": [ { "id": "checked_in", "label": "Checked in on her" }, { "id": "snacks", "label": "Brought her snacks" } ],
                "checkedInToday": true,
                "supports": ["checked_in", "snacks"],
                "reward": 10,
                "streak": 3,
                "pointsAwarded": 10,
                "newBalance": 120
              }
            }
            """;
    static final String CHECK_IN_409_EXAMPLE = """
            {
              "code": 409,
              "message": "You've already checked in today. Come back tomorrow.",
              "data": { "errorCode": "already_claimed" }
            }
            """;
    static final String PARTNER_404_EXAMPLE = """
            {
              "code": 404,
              "message": "You're not sharing with a partner.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String LOVE_SUMMARY = "Her partner's check-ins";
    static final String LOVE_DESCRIPTION = "For the account owner: each day in the range her partner checked in, with what he did (ids and labels) "
            + "and his optional note. Each check-in also lands in her notification inbox as partner_love, and is pushed to her phone unless "
            + "she turned off partnerLove in her notification settings.";
    static final String LOVE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "items": [
                  { "date": "2026-10-24", "partnerName": "Tunde", "supports": ["listened", "snacks"],
                    "labels": ["Listened", "Tea & treats"], "note": "Proud of you today." }
                ]
              }
            }
            """;
}
