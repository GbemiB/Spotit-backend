package com.spotit.api.reminder.push;

public record PushResult(String to, String ticketId, String error) {
    public static final String DEVICE_NOT_REGISTERED = "DeviceNotRegistered";

    public boolean deviceGone() {
        return DEVICE_NOT_REGISTERED.equals(error);
    }
}
