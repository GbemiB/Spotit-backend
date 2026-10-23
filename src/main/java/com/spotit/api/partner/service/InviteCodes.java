package com.spotit.api.partner.service;

import java.security.SecureRandom;
import java.util.Locale;

public final class InviteCodes {
    public static final int LENGTH = 6;

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private InviteCodes() {
    }

    public static String newCode() {
        StringBuilder code = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            code.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }

    public static String normalize(String input) {
        return input == null ? "" : input.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
