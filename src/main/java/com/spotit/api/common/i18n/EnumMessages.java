package com.spotit.api.common.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Resolves the {@code code} carried by every domain enum (Goal, FlowIntensity,
 * ThemePref, ...) into display text via {@code messages.properties}. Enums
 * only know their message-bundle key, not the text itself — adding a language
 * later means adding {@code messages_xx.properties}, not touching the enums.
 */
public final class EnumMessages {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    private EnumMessages() {
    }

    public static String resolve(String code) {
        try {
            return BUNDLE.getString(code);
        } catch (MissingResourceException e) {
            return code;
        }
    }
}
