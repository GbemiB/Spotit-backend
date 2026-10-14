package com.spotit.api.configuration;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public final class ReminderSettingsFixture {
    private ReminderSettingsFixture() {
    }

    public static ReminderSettings defaults() {
        return withPeriodDays(List.of(3, 2, 1, 0));
    }

    public static ReminderSettings withPeriodDays(List<Integer> periodDaysBefore) {
        return new ReminderSettings(true, LocalTime.of(6, 0), LocalTime.of(20, 0), LocalTime.of(19, 0), DayOfWeek.SUNDAY,
                periodDaysBefore, 3, 120);
    }

    public static ReminderSettings disabled() {
        return new ReminderSettings(false, LocalTime.of(6, 0), LocalTime.of(20, 0), LocalTime.of(19, 0), DayOfWeek.SUNDAY,
                List.of(3, 2, 1, 0), 3, 120);
    }
}
