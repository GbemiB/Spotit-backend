package com.spotit.api.log.entity;

public enum MoodType {
    happy(1, "moodType.happy"),
    calm(2, "moodType.calm"),
    energetic(3, "moodType.energetic"),
    neutral(4, "moodType.neutral"),
    sad(5, "moodType.sad"),
    anxious(6, "moodType.anxious"),
    irritable(7, "moodType.irritable"),
    emotional(8, "moodType.emotional");

    private final Integer value;
    private final String code;

    MoodType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static MoodType fromInt(Integer value) {
        return switch (value) {
            case 1 -> happy;
            case 2 -> calm;
            case 3 -> energetic;
            case 4 -> neutral;
            case 5 -> sad;
            case 6 -> anxious;
            case 7 -> irritable;
            case 8 -> emotional;
            default -> throw new IllegalArgumentException("Unknown MoodType value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isHappy() {
        return this == happy;
    }

    public boolean isCalm() {
        return this == calm;
    }

    public boolean isEnergetic() {
        return this == energetic;
    }

    public boolean isNeutral() {
        return this == neutral;
    }

    public boolean isSad() {
        return this == sad;
    }

    public boolean isAnxious() {
        return this == anxious;
    }

    public boolean isIrritable() {
        return this == irritable;
    }

    public boolean isEmotional() {
        return this == emotional;
    }
}
