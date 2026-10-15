package com.spotit.api.reminder.dto;

import java.util.List;

public record NotificationInboxResponse(List<NotificationItemResponse> items, long unreadCount) {
}
