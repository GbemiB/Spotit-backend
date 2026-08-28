package com.spotit.api.smtp.dto;

// Never includes a password — this is purely so an admin can confirm what's configured for
// each role. `backup` being unconfigured is a normal, valid state (no failover provider set).
public record SmtpSettingsStatusResponse(SmtpProviderStatus primary, SmtpProviderStatus backup) {

    public record SmtpProviderStatus(boolean configured, String host, Integer port, String username, String fromAddress, Boolean useTls) {

        public static SmtpProviderStatus unconfigured() {
            return new SmtpProviderStatus(false, null, null, null, null, null);
        }
    }
}
