package com.spotit.api.user.entity;

public enum ThemePref {
    light(1, "themePref.light"),
    dark(2, "themePref.dark"),
    system(3, "themePref.system");

    private final Integer value;
    private final String code;

    ThemePref(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static ThemePref fromInt(Integer value) {
        return switch (value) {
            case 1 -> light;
            case 2 -> dark;
            case 3 -> system;
            default -> throw new IllegalArgumentException("Unknown ThemePref value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isLight() {
        return this == light;
    }

    public boolean isDark() {
        return this == dark;
    }

    public boolean isSystem() {
        return this == system;
    }
}
