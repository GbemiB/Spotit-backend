package com.spotit.api.settings.service;

public interface AppSettingsService {

    /**
     * Returns the single settings row, seeding it with defaults (and a freshly generated JWT
     * secret) on the very first call if none exists yet.
     */
    ResolvedAppSettings getActiveSettings();
}
