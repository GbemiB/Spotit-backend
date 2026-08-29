package com.spotit.api.common.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
