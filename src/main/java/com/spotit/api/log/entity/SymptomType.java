package com.spotit.api.log.entity;

import java.util.List;

public enum SymptomType {
    cramps(1, "symptomType.cramps"),
    headache(2, "symptomType.headache"),
    bloating(3, "symptomType.bloating"),
    tender(4, "symptomType.tender"),
    fatigue(5, "symptomType.fatigue"),
    nausea(6, "symptomType.nausea"),
    backpain(7, "symptomType.backpain"),
    acne(8, "symptomType.acne"),
    moodswings(9, "symptomType.moodswings"),
    insomnia(10, "symptomType.insomnia"),
    discharge(11, "symptomType.discharge"),
    dizziness(12, "symptomType.dizziness"),
    jointpain(13, "symptomType.jointpain"),
    pelvicpain(14, "symptomType.pelvicpain"),
    sweating(15, "symptomType.sweating"),
    anxiety(16, "symptomType.anxiety");

    private final Integer value;
    private final String code;

    SymptomType(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static SymptomType fromInt(Integer value) {
        return switch (value) {
            case 1 -> cramps;
            case 2 -> headache;
            case 3 -> bloating;
            case 4 -> tender;
            case 5 -> fatigue;
            case 6 -> nausea;
            case 7 -> backpain;
            case 8 -> acne;
            case 9 -> moodswings;
            case 10 -> insomnia;
            case 11 -> discharge;
            case 12 -> dizziness;
            case 13 -> jointpain;
            case 14 -> pelvicpain;
            case 15 -> sweating;
            case 16 -> anxiety;
            default -> throw new IllegalArgumentException("Unknown SymptomType value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isCramps() {
        return this == cramps;
    }

    public boolean isHeadache() {
        return this == headache;
    }

    public boolean isBloating() {
        return this == bloating;
    }

    public boolean isTender() {
        return this == tender;
    }

    public boolean isFatigue() {
        return this == fatigue;
    }

    public boolean isNausea() {
        return this == nausea;
    }

    public boolean isBackpain() {
        return this == backpain;
    }

    public boolean isAcne() {
        return this == acne;
    }

    public boolean isMoodswings() {
        return this == moodswings;
    }

    public boolean isInsomnia() {
        return this == insomnia;
    }

    public boolean isDischarge() {
        return this == discharge;
    }

    public boolean isDizziness() {
        return this == dizziness;
    }

    public boolean isJointpain() {
        return this == jointpain;
    }

    public boolean isPelvicpain() {
        return this == pelvicpain;
    }

    public boolean isSweating() {
        return this == sweating;
    }

    public boolean isAnxiety() {
        return this == anxiety;
    }

    public static List<Integer> toValues(List<String> names) {
        if (names == null) {
            return List.of();
        }
        return names.stream().map(name -> SymptomType.valueOf(name).getValue()).toList();
    }

    public static List<String> fromValues(List<Integer> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream().map(value -> SymptomType.fromInt(value).name()).toList();
    }
}
