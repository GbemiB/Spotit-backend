package com.spotit.api.user.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.user.dto.*;
import com.spotit.api.user.service.UserReadService;
import com.spotit.api.user.service.UserWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    @GetMapping
    public UserResponse getProfile(@CurrentUserId UUID userId) {
        return userReadService.getProfile(userId);
    }

    @PatchMapping
    public UserResponse updateProfile(@CurrentUserId UUID userId, @Valid @RequestBody UpdateProfileRequest request) {
        return userWriteService.updateProfile(userId, request);
    }

    @GetMapping("/notifications")
    public NotificationPrefsResponse getNotifications(@CurrentUserId UUID userId) {
        return userReadService.getNotificationPrefs(userId);
    }

    @PatchMapping("/notifications")
    public NotificationPrefsResponse updateNotifications(@CurrentUserId UUID userId, @RequestBody UpdateNotificationPrefsRequest request) {
        return userWriteService.updateNotificationPrefs(userId, request);
    }

    @PostMapping("/export")
    public ExportJobResponse export(@CurrentUserId UUID userId) {
        return userWriteService.requestExport(userId);
    }

    @GetMapping("/export/{jobId}/download")
    public ExportDataResponse downloadExport(@CurrentUserId UUID userId, @PathVariable UUID jobId) {
        return userReadService.getExportData(userId, jobId);
    }

    @PostMapping("/reset")
    public ResetResponse reset(@CurrentUserId UUID userId) {
        return userWriteService.resetAllData(userId);
    }
}
