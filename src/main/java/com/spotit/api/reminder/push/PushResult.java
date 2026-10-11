package com.spotit.api.reminder.push;

/**
 * What the push service said about one message. {@code ticketId} is set when it was accepted; {@code error} carries
 * the service's error code otherwise (for example {@link #DEVICE_NOT_REGISTERED}).
 */
public record PushResult(String to, String ticketId, String error) {
    public static final String DEVICE_NOT_REGISTERED = "DeviceNotRegistered";

    public boolean deviceGone() {
        return DEVICE_NOT_REGISTERED.equals(error);
    }
}
