package com.spotit.api.user.service;

import com.spotit.api.user.dto.*;

import java.util.UUID;

public interface UserWriteService {
    UserResponse updateProfile(UUID userId, UpdateProfileRequest request);

    NotificationPrefsResponse updateNotificationPrefs(UUID userId, UpdateNotificationPrefsRequest request);

    OnboardingResponse completeOnboarding(UUID userId, OnboardingRequest request);

    ExportJobResponse requestExport(UUID userId);

    ResetResponse resetAllData(UUID userId);
}
