package com.spotit.api.user.service;

import com.spotit.api.user.dto.ExportDataResponse;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.dto.UserResponse;

import java.util.UUID;

public interface UserReadService {

    UserResponse getProfile(UUID userId);

    NotificationPrefsResponse getNotificationPrefs(UUID userId);

    ExportDataResponse getExportData(UUID userId, UUID jobId);
}
