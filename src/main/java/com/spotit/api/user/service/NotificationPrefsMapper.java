package com.spotit.api.user.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.entity.User;

import java.util.List;

public final class NotificationPrefsMapper {
    private NotificationPrefsMapper() {
    }

    public static NotificationPrefsResponse toResponse(User user, ReminderSettings settings) {
        return new NotificationPrefsResponse(user.isNotifPeriod(), user.isNotifOvulation(), user.isNotifDailyLog(), user.isNotifDigest(),
                settings.morningTime().toString(), settings.eveningTime().toString(),
                periodDaysBefore(user, settings), settings.maxDaysBefore(), user.isNotifPartnerLoveOn());
    }

    public static List<Integer> periodDaysBefore(User user, ReminderSettings settings) {
        return ReminderSettings.parseDays(user.getPeriodDaysBefore(), settings.maxDaysBefore(), settings.periodDaysBefore());
    }
}
