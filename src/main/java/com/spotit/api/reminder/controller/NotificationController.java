package com.spotit.api.reminder.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.reminder.dto.NotificationInboxResponse;
import com.spotit.api.reminder.service.ReminderReadService;
import com.spotit.api.reminder.service.ReminderWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Notifications", description = "The in-app inbox of reminders sent to the current user.")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final ReminderReadService reminderReadService;
    private final ReminderWriteService reminderWriteService;

    @Operation(summary = NotificationControllerSwagger.INBOX_SUMMARY, description = NotificationControllerSwagger.INBOX_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications.", content = @Content(
                    schema = @Schema(implementation = NotificationInboxResponse.class),
                    examples = @ExampleObject(value = NotificationControllerSwagger.INBOX_200_EXAMPLE)))
    })
    @GetMapping
    public NotificationInboxResponse inbox(@CurrentUserId UUID userId) {
        return reminderReadService.getInbox(userId);
    }

    @Operation(summary = NotificationControllerSwagger.READ_SUMMARY, description = NotificationControllerSwagger.READ_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marked as read.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = NotificationControllerSwagger.READ_200_EXAMPLE)))
    })
    @PostMapping("/read")
    public MessageResponse markRead(@CurrentUserId UUID userId) {
        reminderWriteService.markAllRead(userId);
        return new MessageResponse("Notifications marked as read.");
    }
}
