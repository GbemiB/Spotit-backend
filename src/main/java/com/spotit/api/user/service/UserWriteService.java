package com.spotit.api.user.service;

import com.spotit.api.user.dto.*;

import java.util.UUID;

public interface UserWriteService {

    UserResponse updateProfile(UUID userId, UpdateProfileRequest request);

    NotificationPrefsResponse updateNotificationPrefs(UUID userId, UpdateNotificationPrefsRequest request);

    OnboardingResponse completeOnboarding(UUID userId, OnboardingRequest request);

    ExportJobResponse requestExport(UUID userId);

    /**
     * Wipes cycle logs, points history, badges, and challenge progress, and
     * resets the user's tracking/gamification fields back to defaults —
     * matches the API spec's "reset all data" contract. Deliberately leaves
     * orders, subscription, and devices untouched: those represent real
     * purchases/billing state, not tracking data.
     */
    ResetResponse resetAllData(UUID userId);
}
