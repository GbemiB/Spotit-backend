package com.spotit.api.smtp.service;

/** SMTP settings with the password already decrypted — never persisted or logged in this form. */
public record ResolvedSmtpSettings(String host, int port, String username, String password, String fromAddress, boolean useTls) {
}
