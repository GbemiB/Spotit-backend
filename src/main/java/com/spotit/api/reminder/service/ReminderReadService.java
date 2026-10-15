package com.spotit.api.reminder.service;

import com.spotit.api.reminder.dto.NotificationInboxResponse;

import java.util.UUID;

public interface ReminderReadService {
    NotificationInboxResponse getInbox(UUID userId);
}
