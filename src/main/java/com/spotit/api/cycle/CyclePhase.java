package com.spotit.api.cycle;

public enum CyclePhase {
    period(1, "cyclePhase.period"),
    fertile(3, "cyclePhase.fertile"),
    ovulation(4, "cyclePhase.ovulation");

    private final Integer value;
    private final String code;

    CyclePhase(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static CyclePhase fromInt(Integer value) {
        return switch (value) {
            case 1 -> period;
            case 3 -> fertile;
            case 4 -> ovulation;
            default -> throw new IllegalArgumentException("Unknown CyclePhase value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isPeriod() {
        return this == period;
    }

    public boolean isFertile() {
        return this == fertile;
    }

    public boolean isOvulation() {
        return this == ovulation;
    }
}
