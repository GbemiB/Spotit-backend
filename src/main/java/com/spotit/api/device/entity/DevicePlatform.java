package com.spotit.api.device.entity;

public enum DevicePlatform {
    ios(1, "devicePlatform.ios"),
    android(2, "devicePlatform.android");

    private final Integer value;
    private final String code;

    DevicePlatform(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static DevicePlatform fromInt(Integer value) {
        return switch (value) {
            case 1 -> ios;
            case 2 -> android;
            default -> throw new IllegalArgumentException("Unknown DevicePlatform value: " + value);
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
