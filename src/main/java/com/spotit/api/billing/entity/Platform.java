package com.spotit.api.billing.entity;

public enum Platform {
    ios(1, "platform.ios"),
    android(2, "platform.android");

    private final Integer value;
    private final String code;

    Platform(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static Platform fromInt(Integer value) {
        return switch (value) {
            case 1 -> ios;
            case 2 -> android;
            default -> throw new IllegalArgumentException("Unknown Platform value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isIos() {
        return this == ios;
    }

    public boolean isAndroid() {
        return this == android;
    }
}
