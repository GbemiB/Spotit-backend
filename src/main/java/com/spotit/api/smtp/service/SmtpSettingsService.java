package com.spotit.api.smtp.service;

import com.spotit.api.smtp.entity.SmtpRole;

import java.util.List;

public interface SmtpSettingsService {

    /**
     * Configured providers in send-attempt order — primary first, then backup, omitting
     * whichever role has no row saved. Empty if neither is configured.
     */
    List<ResolvedSmtpSettings> getSettingsInPriorityOrder();

    /**
     * Upserts the settings row for this role. Pass {@code password} as {@code null} to leave
     * the previously stored (encrypted) password unchanged, e.g. when an admin edits only the
     * host.
     */
    void saveSettings(SmtpRole role, String host, int port, String username, String password, String fromAddress, boolean useTls);
}
