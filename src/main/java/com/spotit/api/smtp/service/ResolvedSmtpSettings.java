package com.spotit.api.smtp.service;

import com.spotit.api.smtp.entity.SmtpRole;

/** SMTP settings with the password already decrypted — never persisted or logged in this form. */
public record ResolvedSmtpSettings(SmtpRole role, String host, int port, String username, String password, String fromAddress, boolean useTls) {
}
