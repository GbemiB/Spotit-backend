package com.spotit.api.smtp.service;

import com.spotit.api.smtp.entity.SmtpRole;

public record ResolvedSmtpSettings(SmtpRole role, String host, int port, String username, String password, String fromAddress, boolean useTls) {
}
