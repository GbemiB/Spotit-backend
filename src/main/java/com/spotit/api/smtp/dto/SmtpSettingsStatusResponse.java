package com.spotit.api.smtp.dto;

public record SmtpSettingsStatusResponse(boolean configured, String host, Integer port, String username, String fromAddress, Boolean useTls) {
    public static SmtpSettingsStatusResponse unconfigured() {
        return new SmtpSettingsStatusResponse(false, null, null, null, null, null);
    }
}
