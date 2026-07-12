package com.spotit.api.cycle;

public enum CyclePhase {
    period(1, "cyclePhase.period"),
    follicular(2, "cyclePhase.follicular"),
    fertile(3, "cyclePhase.fertile"),
    ovulation(4, "cyclePhase.ovulation"),
    luteal(5, "cyclePhase.luteal");

    private final Integer value;
    private final String code;

    CyclePhase(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static CyclePhase fromInt(Integer value) {
        return switch (value) {
            case 1 -> period;
            case 2 -> follicular;
            case 3 -> fertile;
            case 4 -> ovulation;
            case 5 -> luteal;
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

    public boolean isFollicular() {
        return this == follicular;
    }

    public boolean isFertile() {
        return this == fertile;
    }

    public boolean isOvulation() {
        return this == ovulation;
    }

    public boolean isLuteal() {
        return this == luteal;
    }
}
