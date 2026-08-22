package com.spotit.api.smtp.service;

import java.util.Optional;

public interface SmtpSettingsService {

    /** Empty when no DB-backed settings have been configured yet — callers should fall back to env-var config. */
    Optional<ResolvedSmtpSettings> getActiveSettings();

    /**
     * Upserts the single settings row. Pass {@code password} as {@code null} to leave the
     * previously stored (encrypted) password unchanged, e.g. when an admin edits only the host.
     */
    void saveSettings(String host, int port, String username, String password, String fromAddress, boolean useTls);
}
