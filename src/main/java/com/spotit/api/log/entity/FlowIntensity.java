package com.spotit.api.log.entity;

public enum FlowIntensity {
    spotting(1, "flowIntensity.spotting"),
    light(2, "flowIntensity.light"),
    medium(3, "flowIntensity.medium"),
    heavy(4, "flowIntensity.heavy");

    private final Integer value;
    private final String code;

    FlowIntensity(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static FlowIntensity fromInt(Integer value) {
        return switch (value) {
            case 1 -> spotting;
            case 2 -> light;
            case 3 -> medium;
            case 4 -> heavy;
            default -> throw new IllegalArgumentException("Unknown FlowIntensity value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isSpotting() {
        return this == spotting;
    }

    public boolean isLight() {
        return this == light;
    }

    public boolean isMedium() {
        return this == medium;
    }

    public boolean isHeavy() {
        return this == heavy;
    }
}
