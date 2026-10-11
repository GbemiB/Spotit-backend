package com.spotit.api.configuration;

import com.spotit.api.configuration.entity.GlobalConfig;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The reminder settings held in {@code global_config}, parsed and ready to use. The admin API stores them as
 * free text with no validation, so anything missing or unreadable falls back to {@link ConfigDefaults}
 * rather than stopping the reminder job.
 */
public record ReminderSettings(boolean enabled, LocalTime morningTime, LocalTime eveningTime, LocalTime digestTime,
                               DayOfWeek digestDay, List<Integer> periodDaysBefore, List<Integer> ovulationDaysBefore,
                               int maxDaysBefore, int catchUpMinutes) {

    public static ReminderSettings from(GlobalConfig global) {
        int maxDays = positive(global.getReminderMaxDaysBefore(), ConfigDefaults.REMINDER_MAX_DAYS_BEFORE);
        List<Integer> fallbackDays = parseDays(ConfigDefaults.REMINDER_DAYS_BEFORE, maxDays, List.of(0));
        return new ReminderSettings(
                !Boolean.FALSE.equals(global.getRemindersEnabled()),
                parseTime(global.getReminderMorningTime(), ConfigDefaults.REMINDER_MORNING_TIME),
                parseTime(global.getReminderEveningTime(), ConfigDefaults.REMINDER_EVENING_TIME),
                parseTime(global.getReminderDigestTime(), ConfigDefaults.REMINDER_DIGEST_TIME),
                parseDay(global.getReminderDigestDay()),
                parseDays(global.getReminderPeriodDaysBefore(), maxDays, fallbackDays),
                parseDays(global.getReminderOvulationDaysBefore(), maxDays, fallbackDays),
                maxDays,
                positive(global.getReminderCatchUpMinutes(), ConfigDefaults.REMINDER_CATCH_UP_MINUTES));
    }

    /** Parses "HH:mm"; returns the fallback (also "HH:mm") when the value is missing or unreadable. */
    public static LocalTime parseTime(String value, String fallback) {
        try {
            return LocalTime.parse(value == null ? fallback : value.trim());
        } catch (DateTimeParseException e) {
            return LocalTime.parse(fallback);
        }
    }

    /** Parses "3,2,1,0" into distinct days within 0..maxDays, largest first; the fallback when nothing usable is left. */
    public static List<Integer> parseDays(String value, int maxDays, List<Integer> fallback) {
        if (value == null) {
            return fallback;
        }
        try {
            List<Integer> days = Arrays.stream(value.split(","))
                    .map(String::trim)
                    .filter(part -> !part.isEmpty())
                    .map(Integer::valueOf)
                    .filter(day -> day >= 0 && day <= maxDays)
                    .distinct()
                    .sorted(Comparator.reverseOrder())
                    .toList();
            return days.isEmpty() ? fallback : days;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static String formatDays(List<Integer> days) {
        return String.join(",", days.stream().distinct().sorted(Comparator.reverseOrder()).map(String::valueOf).toList());
    }

    private static DayOfWeek parseDay(Long value) {
        long day = value == null || value < 1 || value > 7 ? ConfigDefaults.REMINDER_DIGEST_DAY : value;
        return DayOfWeek.of((int) day);
    }

    private static int positive(Long value, long fallback) {
        return (int) (value == null || value <= 0 ? fallback : value);
    }
}
