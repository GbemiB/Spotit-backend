package com.spotit.api.user.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.entity.User;

import java.time.LocalTime;
import java.util.List;

/** Resolves a user's reminder choices against the server defaults. Shared by the preferences API and the reminder job. */
public final class NotificationPrefsMapper {
    private NotificationPrefsMapper() {
    }

    public static NotificationPrefsResponse toResponse(User user, ReminderSettings settings) {
        return new NotificationPrefsResponse(user.isNotifPeriod(), user.isNotifOvulation(), user.isNotifDailyLog(), user.isNotifDigest(),
                morningTime(user, settings).toString(), eveningTime(user, settings).toString(),
                periodDaysBefore(user, settings), settings.maxDaysBefore());
    }

    public static LocalTime morningTime(User user, ReminderSettings settings) {
        return ReminderSettings.parseTime(user.getReminderMorningTime(), settings.morningTime().toString());
    }

    public static LocalTime eveningTime(User user, ReminderSettings settings) {
        return ReminderSettings.parseTime(user.getReminderEveningTime(), settings.eveningTime().toString());
    }

    public static List<Integer> periodDaysBefore(User user, ReminderSettings settings) {
        return ReminderSettings.parseDays(user.getPeriodDaysBefore(), settings.maxDaysBefore(), settings.periodDaysBefore());
    }
}
