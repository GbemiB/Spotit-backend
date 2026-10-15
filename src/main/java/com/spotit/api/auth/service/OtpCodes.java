package com.spotit.api.auth.service;

import java.security.SecureRandom;

public final class OtpCodes {

    public static final int DIGITS = 4;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int BOUND = (int) Math.pow(10, DIGITS);
    private static final String FORMAT = "%0" + DIGITS + "d";

    private OtpCodes() {
    }

    public static String newCode() {
        return FORMAT.formatted(RANDOM.nextInt(BOUND));
    }
}
