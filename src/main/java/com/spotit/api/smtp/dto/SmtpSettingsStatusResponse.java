package com.spotit.api.smtp.dto;

public record SmtpSettingsStatusResponse(SmtpProviderStatus primary, SmtpProviderStatus backup) {
    public record SmtpProviderStatus(boolean configured, String host, Integer port, String username, String fromAddress, Boolean useTls) {
        public static SmtpProviderStatus unconfigured() {
            return new SmtpProviderStatus(false, null, null, null, null, null);
        }
    }
}
