package com.spotit.api.auth.entity;

public enum OtpPurpose {
    signup(1, "otpPurpose.signup"),
    password_reset(2, "otpPurpose.passwordReset");

    private final Integer value;
    private final String code;

    OtpPurpose(Integer value, String code) {
        this.value = value;
        this.code = code;
    }

    public static OtpPurpose fromInt(Integer value) {
        return switch (value) {
            case 1 -> signup;
            case 2 -> password_reset;
            default -> throw new IllegalArgumentException("Unknown OtpPurpose value: " + value);
        };
    }

    public Integer getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public boolean isSignup() {
        return this == signup;
    }

    public boolean isPasswordReset() {
        return this == password_reset;
    }
}
