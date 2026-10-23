package com.spotit.api.partner.entity;

public enum SupportType {
    checked_in(1, "supportType.checkedIn"),
    snacks(2, "supportType.snacks"),
    massage(3, "supportType.massage"),
    chores(4, "supportType.chores"),
    quiet_time(5, "supportType.quietTime"),
    listened(6, "supportType.listened");

    private final Integer value;
    private final String code;

    SupportType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static SupportType fromInt(Integer value) {
        return switch (value) {
            case 1 -> checked_in;
            case 2 -> snacks;
            case 3 -> massage;
            case 4 -> chores;
            case 5 -> quiet_time;
            case 6 -> listened;
            default -> throw new IllegalArgumentException("Unknown SupportType value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
