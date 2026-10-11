package com.spotit.api.device.controller;

final class DeviceControllerSwagger {
    private DeviceControllerSwagger() {
    }

    static final String REGISTER_SUMMARY = "Register a device";
    static final String REGISTER_DESCRIPTION = "Registers (or re-associates) an Expo push token with the current user. "
            + "The optional time zone sets the local time reminders are sent at.";
    static final String REGISTER_REQUEST_EXAMPLE = """
            {
              "pushToken": "ExponentPushToken[xxxxxxxxxxxxxxxxxxxxxx]",
              "platform": "android",
              "timeZone": "Africa/Lagos"
            }
            """;
    static final String REGISTER_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Device registered.",
              "data": null
            }
            """;

    static final String UNREGISTER_SUMMARY = "Unregister a device";
    static final String UNREGISTER_DESCRIPTION = "Removes a push token, e.g. on logout or app uninstall detection.";
    static final String UNREGISTER_200_EXAMPLE = """
            {
              "code": 200,
              "message": "Device unregistered.",
              "data": null
            }
            """;
}
