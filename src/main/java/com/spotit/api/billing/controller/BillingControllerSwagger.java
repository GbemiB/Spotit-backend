package com.spotit.api.billing.controller;

final class BillingControllerSwagger {
    private BillingControllerSwagger() {
    }

    static final String STATUS_SUMMARY = "Get subscription status";
    static final String STATUS_DESCRIPTION = "Returns the current user's subscription status. If the user has never subscribed, returns a non-premium status with null plan.";
    static final String STATUS_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "isPremium": true,
                "plan": "annual",
                "renewsAt": "2026-08-09T00:00:00Z",
                "autoRenew": true
              }
            }
            """;

    static final String SUBSCRIBE_SUMMARY = "Subscribe to Premium";
    static final String SUBSCRIBE_DESCRIPTION = "Verifies a store receipt and activates a Premium subscription for the current user.";
    static final String SUBSCRIBE_REQUEST_EXAMPLE = """
            {
              "planId": "annual",
              "platform": "ios",
              "receipt": "MIISvAYJKoZIhvcNAQcCoIISrTC..."
            }
            """;
    static final String SUBSCRIBE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "isPremium": true,
                "plan": "annual",
                "renewsAt": "2026-08-09T00:00:00Z",
                "autoRenew": true
              }
            }
            """;
    static final String SUBSCRIBE_402_EXAMPLE = """
            {
              "code": 402,
              "message": "Receipt could not be verified.",
              "data": { "errorCode": "receipt_invalid" }
            }
            """;
    static final String SUBSCRIBE_409_EXAMPLE = """
            {
              "code": 409,
              "message": "You already have an active subscription.",
              "data": { "errorCode": "already_subscribed" }
            }
            """;

    static final String CANCEL_SUMMARY = "Cancel subscription";
    static final String CANCEL_DESCRIPTION = "Turns off auto-renew for the current user's subscription. Access continues until the current period ends.";
    static final String CANCEL_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "isPremium": true,
                "autoRenew": false,
                "accessUntil": "2026-08-09T00:00:00Z"
              }
            }
            """;
    static final String CANCEL_404_EXAMPLE = """
            {
              "code": 404,
              "message": "No subscription found.",
              "data": { "errorCode": "not_found" }
            }
            """;

    static final String RESTORE_SUMMARY = "Restore purchase";
    static final String RESTORE_DESCRIPTION = "Re-verifies a store receipt for a user who reinstalled the app or switched devices and reactivates their existing subscription.";
    static final String RESTORE_REQUEST_EXAMPLE = """
            {
              "platform": "ios",
              "receipt": "MIISvAYJKoZIhvcNAQcCoIISrTC..."
            }
            """;
    static final String RESTORE_200_EXAMPLE = """
            {
              "code": 200,
              "message": "OK",
              "data": {
                "isPremium": true,
                "plan": "annual",
                "renewsAt": "2026-08-09T00:00:00Z",
                "autoRenew": true
              }
            }
            """;
    static final String RESTORE_404_EXAMPLE = """
            {
              "code": 404,
              "message": "No previous purchase found for this account.",
              "data": { "errorCode": "no_purchase_found" }
            }
            """;
}
