package com.spotit.api.configuration;

import com.spotit.api.configuration.entity.GlobalConfig;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderSettingsTest {
    @Test
    void anUnseededRowGivesTheDefaults() {
        ReminderSettings settings = ReminderSettings.from(new GlobalConfig());

        assertThat(settings.enabled()).isTrue();
        assertThat(settings.morningTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(settings.eveningTime()).isEqualTo(LocalTime.of(20, 0));
        assertThat(settings.digestTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(settings.digestDay()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(settings.periodDaysBefore()).containsExactly(3, 2, 1, 0);
        assertThat(settings.ovulationDaysBefore()).containsExactly(3, 2, 1, 0);
        assertThat(settings.maxDaysBefore()).isEqualTo(3);
        assertThat(settings.catchUpMinutes()).isEqualTo(120);
    }

    @Test
    void storedValuesAreUsed() {
        ReminderSettings settings = ReminderSettings.from(GlobalConfig.builder()
                .remindersEnabled(false).reminderMorningTime("07:30").reminderEveningTime("21:15").reminderDigestTime("10:00")
                .reminderDigestDay(1L).reminderPeriodDaysBefore("5, 1,0").reminderOvulationDaysBefore("0")
                .reminderMaxDaysBefore(5L).reminderCatchUpMinutes(30L).build());

        assertThat(settings.enabled()).isFalse();
        assertThat(settings.morningTime()).isEqualTo(LocalTime.of(7, 30));
        assertThat(settings.eveningTime()).isEqualTo(LocalTime.of(21, 15));
        assertThat(settings.digestTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(settings.digestDay()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(settings.periodDaysBefore()).containsExactly(5, 1, 0);
        assertThat(settings.ovulationDaysBefore()).containsExactly(0);
        assertThat(settings.maxDaysBefore()).isEqualTo(5);
        assertThat(settings.catchUpMinutes()).isEqualTo(30);
    }

    // The admin API stores these as free text, so a typo there must not stop the reminder job.
    @Test
    void unreadableValuesFallBackToTheDefaults() {
        ReminderSettings settings = ReminderSettings.from(GlobalConfig.builder()
                .reminderMorningTime("9am").reminderDigestDay(9L).reminderPeriodDaysBefore("three,two")
                .reminderOvulationDaysBefore("8,9").reminderMaxDaysBefore(0L).reminderCatchUpMinutes(-5L).build());

        assertThat(settings.morningTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(settings.digestDay()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(settings.periodDaysBefore()).containsExactly(3, 2, 1, 0);
        assertThat(settings.ovulationDaysBefore()).containsExactly(3, 2, 1, 0);
        assertThat(settings.maxDaysBefore()).isEqualTo(3);
        assertThat(settings.catchUpMinutes()).isEqualTo(120);
    }

    @Test
    void daysAreDeduplicatedSortedAndLimitedToTheMaximum() {
        assertThat(ReminderSettings.parseDays("0,2,2,7,-1", 3, List.of(0))).containsExactly(2, 0);
        assertThat(ReminderSettings.parseDays(null, 3, List.of(1))).containsExactly(1);
        assertThat(ReminderSettings.parseDays(" , ", 3, List.of(1))).containsExactly(1);
        assertThat(ReminderSettings.formatDays(List.of(0, 3, 1, 3))).isEqualTo("3,1,0");
    }
}
