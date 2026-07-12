package com.spotit.api.billing.entity;

public enum SubscriptionStatus {
    active(1, "subscriptionStatus.active"),
    cancelled(2, "subscriptionStatus.cancelled"),
    expired(3, "subscriptionStatus.expired");

    private final Integer value;
    private final String code;

    SubscriptionStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static SubscriptionStatus fromInt(Integer value) {
        return switch (value) {
            case 1 -> active;
            case 2 -> cancelled;
            case 3 -> expired;
            default -> throw new IllegalArgumentException("Unknown SubscriptionStatus value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return this == active;
    }

    public boolean isCancelled() {
        return this == cancelled;
    }

    public boolean isExpired() {
        return this == expired;
    }
}
