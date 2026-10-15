package com.spotit.api.reminder.dto;

import java.time.Instant;
import java.util.UUID;

public record NotificationItemResponse(UUID id, String type, String title, String body, String screen, Instant sentAt, boolean read) {
}
