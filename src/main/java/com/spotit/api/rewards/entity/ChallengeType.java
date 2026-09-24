package com.spotit.api.rewards.entity;

public enum ChallengeType {
    WEEKLY_LOG(1, "challengeType.weeklyLog"),
    STATIC(2, "challengeType.static");

    private final Integer value;
    private final String code;

    ChallengeType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static ChallengeType fromInt(Integer value) {
        return switch (value) {
            case 1 -> WEEKLY_LOG;
            case 2 -> STATIC;
            default -> throw new IllegalArgumentException("Unknown ChallengeType value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isWeeklyLog() {
        return this == WEEKLY_LOG;
    }

    public boolean isStatic() {
        return this == STATIC;
    }
}
