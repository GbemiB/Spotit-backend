package com.spotit.api.shop.entity;

public enum OrderStatus {
    processing(1, "orderStatus.processing"),
    shipped(2, "orderStatus.shipped"),
    delivered(3, "orderStatus.delivered"),
    cancelled(4, "orderStatus.cancelled");

    private final Integer value;
    private final String code;

    OrderStatus(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static OrderStatus fromInt(Integer value) {
        return switch (value) {
            case 1 -> processing;
            case 2 -> shipped;
            case 3 -> delivered;
            case 4 -> cancelled;
            default -> throw new IllegalArgumentException("Unknown OrderStatus value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isProcessing() {
        return this == processing;
    }

    public boolean isShipped() {
        return this == shipped;
    }

    public boolean isDelivered() {
        return this == delivered;
    }

    public boolean isCancelled() {
        return this == cancelled;
    }
}
