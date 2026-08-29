package com.spotit.api.device.controller;

final class DeviceControllerSwagger {
    private DeviceControllerSwagger() {
    }

    static final String REGISTER_SUMMARY = "Register a device";
    static final String REGISTER_DESCRIPTION = "Registers (or re-associates) a push token with the current user, for sending push notifications.";
    static final String REGISTER_REQUEST_EXAMPLE = """
            {
              "pushToken": "fcm:d7f3a1b2-token-example",
              "platform": "android"
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
