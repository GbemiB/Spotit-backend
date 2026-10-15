package com.spotit.api.user.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.configuration.ReminderSettingsFixture;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationPrefsMapperTest {
    ReminderSettings settings = ReminderSettingsFixture.defaults();

    @Test
    void aUserWhoHasNotChosenGetsTheServerCountdown() {
        assertThat(NotificationPrefsMapper.periodDaysBefore(User.builder().build(), settings)).containsExactly(3, 2, 1, 0);
    }

    @Test
    void herOwnPeriodCountdownIsUsed() {
        assertThat(NotificationPrefsMapper.periodDaysBefore(User.builder().periodDaysBefore("1,0").build(), settings)).containsExactly(1, 0);
    }

    @Test
    void anUnreadableStoredCountdownFallsBackToTheServerDefault() {
        assertThat(NotificationPrefsMapper.periodDaysBefore(User.builder().periodDaysBefore("x").build(), settings)).containsExactly(3, 2, 1, 0);
    }

    @Test
    void theResponseCarriesEverySwitchTheServerTimesAndHerCountdown() {
        User user = User.builder().notifPeriod(true).notifOvulation(false).notifDailyLog(true).notifDigest(false)
                .periodDaysBefore("0").build();

        assertThat(NotificationPrefsMapper.toResponse(user, settings)).isEqualTo(
                new NotificationPrefsResponse(true, false, true, false, "06:00", "20:00", List.of(0), 3));
    }
}
