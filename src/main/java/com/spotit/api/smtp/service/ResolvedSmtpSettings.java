package com.spotit.api.smtp.service;

public record ResolvedSmtpSettings(String host, int port, String username, String password, String fromAddress, boolean useTls) {
}
