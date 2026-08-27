package com.spotit.api.smtp.dto;

// Never includes the password — this is purely so an admin can confirm what's configured.
public record SmtpSettingsStatusResponse(boolean configured, String host, Integer port, String username, String fromAddress, Boolean useTls) {

    public static SmtpSettingsStatusResponse unconfigured() {
        return new SmtpSettingsStatusResponse(false, null, null, null, null, null);
    }
}
