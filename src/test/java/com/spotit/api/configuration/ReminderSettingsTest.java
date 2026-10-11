package com.spotit.api.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.convert.ApplicationConversionService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Binds {@code spotit.reminders.*} the way Spring does at start-up, from plain text as an env var would give it. */
class ReminderSettingsTest {

    private static Map<String, String> shipped() {
        Map<String, String> values = new HashMap<>();
        values.put("spotit.reminders.enabled", "true");
        values.put("spotit.reminders.morning-time", "06:00");
        values.put("spotit.reminders.evening-time", "20:00");
        values.put("spotit.reminders.digest-time", "19:00");
        values.put("spotit.reminders.digest-day", "SUNDAY");
        values.put("spotit.reminders.period-days-before", "3,2,1,0");
        values.put("spotit.reminders.max-days-before", "3");
        values.put("spotit.reminders.catch-up-minutes", "120");
        return values;
    }

    private static ReminderSettings bind(Map<String, String> values) {
        Binder binder = new Binder(List.of(new MapConfigurationPropertySource(values)), null,
                ApplicationConversionService.getSharedInstance());
        return binder.bindOrCreate("spotit.reminders", ReminderSettings.class);
    }

    @Test
    void theShippedValuesBind() {
        ReminderSettings settings = bind(shipped());

        assertThat(settings).isEqualTo(ReminderSettingsFixture.defaults());
        assertThat(settings.morningTime()).isEqualTo(LocalTime.of(6, 0));
        assertThat(settings.digestDay()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(settings.digestTime()).isEqualTo(LocalTime.of(19, 0));
    }

    @Test
    void eachValueCanBeOverridden() {
        Map<String, String> values = shipped();
        values.put("spotit.reminders.enabled", "false");
        values.put("spotit.reminders.morning-time", "07:30");
        values.put("spotit.reminders.digest-day", "monday");
        values.put("spotit.reminders.period-days-before", "0, 5,1,1");
        values.put("spotit.reminders.max-days-before", "5");
        values.put("spotit.reminders.catch-up-minutes", "30");

        ReminderSettings settings = bind(values);

        assertThat(settings.enabled()).isFalse();
        assertThat(settings.morningTime()).isEqualTo(LocalTime.of(7, 30));
        assertThat(settings.digestDay()).isEqualTo(DayOfWeek.MONDAY);
        // Stored largest first with repeats removed, as the countdown is read.
        assertThat(settings.periodDaysBefore()).containsExactly(5, 1, 0);
        assertThat(settings.maxDaysBefore()).isEqualTo(5);
        assertThat(settings.catchUpMinutes()).isEqualTo(30);
    }

    @Test
    void anUnreadableTimeStopsTheStart() {
        Map<String, String> values = shipped();
        values.put("spotit.reminders.morning-time", "6am");

        assertThatThrownBy(() -> bind(values)).isInstanceOf(BindException.class);
    }

    @Test
    void anUnknownDayStopsTheStart() {
        Map<String, String> values = shipped();
        values.put("spotit.reminders.digest-day", "8");

        assertThatThrownBy(() -> bind(values)).isInstanceOf(BindException.class);
    }

    @Test
    void aCountdownDayAboveTheMaximumStopsTheStartAndSaysWhich() {
        Map<String, String> values = shipped();
        values.put("spotit.reminders.period-days-before", "4,0");

        assertThatThrownBy(() -> bind(values)).isInstanceOf(BindException.class)
                .rootCause().hasMessageContaining("spotit.reminders.period-days-before");
    }

    @Test
    void aMissingValueStopsTheStart() {
        Map<String, String> values = shipped();
        values.remove("spotit.reminders.evening-time");

        assertThatThrownBy(() -> bind(values)).isInstanceOf(BindException.class)
                .rootCause().hasMessageContaining("spotit.reminders.evening-time");
    }

    @Test
    void aZeroCatchUpWindowStopsTheStart() {
        Map<String, String> values = shipped();
        values.put("spotit.reminders.catch-up-minutes", "0");

        assertThatThrownBy(() -> bind(values)).isInstanceOf(BindException.class)
                .rootCause().hasMessageContaining("spotit.reminders.catch-up-minutes");
    }

    @Test
    void usersStoredDaysAreDeduplicatedSortedAndLimitedToTheMaximum() {
        assertThat(ReminderSettings.parseDays("0,2,2,7,-1", 3, List.of(0))).containsExactly(2, 0);
        assertThat(ReminderSettings.parseDays(null, 3, List.of(1))).containsExactly(1);
        assertThat(ReminderSettings.parseDays(" , ", 3, List.of(1))).containsExactly(1);
        assertThat(ReminderSettings.formatDays(List.of(0, 3, 1, 3))).isEqualTo("3,1,0");
    }
}
