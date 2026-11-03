package com.spotit.api.partner.entity;

public enum PartnerLinkStatus {
    pending(1, "partnerLinkStatus.pending"),
    active(2, "partnerLinkStatus.active"),
    cancelled(3, "partnerLinkStatus.cancelled"),
    ended(4, "partnerLinkStatus.ended");

    private final Integer value;
    private final String code;

    PartnerLinkStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static PartnerLinkStatus fromInt(Integer value) {
        return switch (value) {
            case 1 -> pending;
            case 2 -> active;
            case 3 -> cancelled;
            case 4 -> ended;
            default -> throw new IllegalArgumentException("Unknown PartnerLinkStatus value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isPending() {
        return this == pending;
    }

    public boolean isActive() {
        return this == active;
    }
}
