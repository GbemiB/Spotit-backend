package com.spotit.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The reminder settings from the properties file ({@code spotit.reminders.*} in application.yml, each overridable
 * by an environment variable). They are checked when the server starts: a value that cannot be used stops the
 * start-up with a message naming it, rather than reminders quietly going out at the wrong time.
 *
 * <p>Times are local to each user. The period countdown counts down to the day itself (0) and are stored largest first.
 */
@ConfigurationProperties(prefix = "spotit.reminders")
public record ReminderSettings(boolean enabled,
                               @DateTimeFormat(pattern = "HH:mm") LocalTime morningTime,
                               @DateTimeFormat(pattern = "HH:mm") LocalTime eveningTime,
                               @DateTimeFormat(pattern = "HH:mm") LocalTime digestTime,
                               DayOfWeek digestDay, List<Integer> periodDaysBefore,
                               int maxDaysBefore, int catchUpMinutes) {

    public ReminderSettings {
        require(morningTime != null, "morning-time");
        require(eveningTime != null, "evening-time");
        require(digestTime != null, "digest-time");
        require(digestDay != null, "digest-day");
        require(maxDaysBefore >= 0, "max-days-before");
        require(catchUpMinutes > 0, "catch-up-minutes");
        periodDaysBefore = checkedDays(periodDaysBefore, maxDaysBefore, "period-days-before");
    }

    private static void require(boolean ok, String property) {
        if (!ok) {
            throw new IllegalArgumentException("spotit.reminders." + property + " is missing or invalid");
        }
    }

    private static List<Integer> checkedDays(List<Integer> days, int maxDays, String property) {
        require(days != null && !days.isEmpty() && days.stream().allMatch(day -> day != null && day >= 0 && day <= maxDays),
                property + " (each day must be between 0 and max-days-before)");
        return days.stream().distinct().sorted(Comparator.reverseOrder()).toList();
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
}
