package com.spotit.api.user.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.user.dto.*;
import com.spotit.api.user.service.UserReadService;
import com.spotit.api.user.service.UserWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User", description = "Current user's profile, notification preferences, data export, and data reset.")
@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    @Operation(summary = UserControllerSwagger.GET_PROFILE_SUMMARY, description = UserControllerSwagger.GET_PROFILE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile.", content = @Content(
                    schema = @Schema(implementation = UserResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.GET_PROFILE_200_EXAMPLE)))
    })
    @GetMapping
    public UserResponse getProfile(@CurrentUserId UUID userId) {
        return userReadService.getProfile(userId);
    }

    @Operation(summary = UserControllerSwagger.UPDATE_PROFILE_SUMMARY, description = UserControllerSwagger.UPDATE_PROFILE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = UpdateProfileRequest.class),
            examples = @ExampleObject(value = UserControllerSwagger.UPDATE_PROFILE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated.", content = @Content(
                    schema = @Schema(implementation = UserResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.UPDATE_PROFILE_200_EXAMPLE))),
            @ApiResponse(responseCode = "422", description = "cycleLength must be between 21 and 45, or periodLength must be between 2 and 10.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = UserControllerSwagger.UPDATE_PROFILE_422_EXAMPLE)))
    })
    @PatchMapping
    public UserResponse updateProfile(@CurrentUserId UUID userId, @Valid @RequestBody UpdateProfileRequest request) {
        return userWriteService.updateProfile(userId, request);
    }

    @Operation(summary = UserControllerSwagger.GET_NOTIFICATIONS_SUMMARY, description = UserControllerSwagger.GET_NOTIFICATIONS_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification preferences.", content = @Content(
                    schema = @Schema(implementation = NotificationPrefsResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.GET_NOTIFICATIONS_200_EXAMPLE)))
    })
    @GetMapping("/notifications")
    public NotificationPrefsResponse getNotifications(@CurrentUserId UUID userId) {
        return userReadService.getNotificationPrefs(userId);
    }

    @Operation(summary = UserControllerSwagger.UPDATE_NOTIFICATIONS_SUMMARY, description = UserControllerSwagger.UPDATE_NOTIFICATIONS_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = UpdateNotificationPrefsRequest.class),
            examples = @ExampleObject(value = UserControllerSwagger.UPDATE_NOTIFICATIONS_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification preferences updated.", content = @Content(
                    schema = @Schema(implementation = NotificationPrefsResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.UPDATE_NOTIFICATIONS_200_EXAMPLE)))
    })
    @PatchMapping("/notifications")
    public NotificationPrefsResponse updateNotifications(@CurrentUserId UUID userId, @RequestBody UpdateNotificationPrefsRequest request) {
        return userWriteService.updateNotificationPrefs(userId, request);
    }

    @Operation(summary = UserControllerSwagger.EXPORT_SUMMARY, description = UserControllerSwagger.EXPORT_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Export job created.", content = @Content(
                    schema = @Schema(implementation = ExportJobResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.EXPORT_200_EXAMPLE)))
    })
    @PostMapping("/export")
    public ExportJobResponse export(@CurrentUserId UUID userId) {
        return userWriteService.requestExport(userId);
    }

    @Operation(summary = UserControllerSwagger.DOWNLOAD_EXPORT_SUMMARY, description = UserControllerSwagger.DOWNLOAD_EXPORT_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Export data.", content = @Content(
                    schema = @Schema(implementation = ExportDataResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.DOWNLOAD_EXPORT_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Export job not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = UserControllerSwagger.DOWNLOAD_EXPORT_404_EXAMPLE)))
    })
    @GetMapping("/export/{jobId}/download")
    public ExportDataResponse downloadExport(@CurrentUserId UUID userId, @Parameter(description = "Export job id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID jobId) {
        return userReadService.getExportData(userId, jobId);
    }

    @Operation(summary = UserControllerSwagger.RESET_SUMMARY, description = UserControllerSwagger.RESET_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All data reset.", content = @Content(
                    schema = @Schema(implementation = ResetResponse.class),
                    examples = @ExampleObject(value = UserControllerSwagger.RESET_200_EXAMPLE)))
    })
    @PostMapping("/reset")
    public ResetResponse reset(@CurrentUserId UUID userId) {
        return userWriteService.resetAllData(userId);
    }
}
