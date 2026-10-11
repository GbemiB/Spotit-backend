package com.spotit.api.user.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.configuration.entity.GlobalConfig;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationPrefsMapperTest {
    ReminderSettings settings = ReminderSettings.from(new GlobalConfig());

    @Test
    void aUserWhoHasNotChosenGetsTheServerDefaults() {
        User user = User.builder().build();

        assertThat(NotificationPrefsMapper.morningTime(user, settings)).isEqualTo(LocalTime.of(9, 0));
        assertThat(NotificationPrefsMapper.eveningTime(user, settings)).isEqualTo(LocalTime.of(20, 0));
        assertThat(NotificationPrefsMapper.periodDaysBefore(user, settings)).containsExactly(3, 2, 1, 0);
        assertThat(NotificationPrefsMapper.ovulationDaysBefore(user, settings)).containsExactly(3, 2, 1, 0);
    }

    @Test
    void periodAndOvulationDaysAreKeptSeparately() {
        User user = User.builder().periodDaysBefore("1,0").ovulationDaysBefore("3").build();

        assertThat(NotificationPrefsMapper.periodDaysBefore(user, settings)).containsExactly(1, 0);
        assertThat(NotificationPrefsMapper.ovulationDaysBefore(user, settings)).containsExactly(3);
    }

    @Test
    void unreadableStoredChoicesFallBackToTheServerDefaults() {
        User user = User.builder().reminderMorningTime("9am").reminderEveningTime("").periodDaysBefore("x").ovulationDaysBefore("9").build();

        assertThat(NotificationPrefsMapper.morningTime(user, settings)).isEqualTo(LocalTime.of(9, 0));
        assertThat(NotificationPrefsMapper.eveningTime(user, settings)).isEqualTo(LocalTime.of(20, 0));
        assertThat(NotificationPrefsMapper.periodDaysBefore(user, settings)).containsExactly(3, 2, 1, 0);
        // 9 is beyond the allowed maximum of 3, so nothing usable is left.
        assertThat(NotificationPrefsMapper.ovulationDaysBefore(user, settings)).containsExactly(3, 2, 1, 0);
    }

    @Test
    void theResponseCarriesEverySwitchTimeAndDayList() {
        User user = User.builder().notifPeriod(true).notifOvulation(false).notifDailyLog(true).notifDigest(false)
                .reminderMorningTime("07:30").periodDaysBefore("0").build();

        assertThat(NotificationPrefsMapper.toResponse(user, settings)).isEqualTo(
                new NotificationPrefsResponse(true, false, true, false, "07:30", "20:00", List.of(0), List.of(3, 2, 1, 0), 3));
    }
}
