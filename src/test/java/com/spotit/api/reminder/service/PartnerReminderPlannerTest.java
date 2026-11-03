package com.spotit.api.reminder.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.configuration.ReminderSettingsFixture;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.user.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PartnerReminderPlannerTest {
    static final LocalDate LAST_PERIOD = LocalDate.of(2026, 10, 5);
    static final LocalDate NEXT_PERIOD = LocalDate.of(2026, 11, 2);
    static final LocalDate FIVE_DAYS_BEFORE = LocalDate.of(2026, 10, 28);

    PartnerReminderPlanner planner = new PartnerReminderPlanner();
    ReminderSettings settings = ReminderSettingsFixture.defaults();

    User owner(LocalDate lastPeriod) {
        return User.builder().firstName("Amaka").lastPeriodDate(lastPeriod).cycleLength(28).periodLength(5).build();
    }

    PartnerLink link(String nickname) {
        return PartnerLink.builder().partnerNickname(nickname).partnerReminder(true).build();
    }

    Optional<PartnerReminderPlanner.Planned> plan(PartnerLink link, User owner, LocalDate date, int hour, int minute) {
        return planner.plan(link, owner, settings, date.atTime(hour, minute));
    }

    @Test
    void fiveDaysBeforeAtSixInTheMorningItUsesTheSavedName() {
        Optional<PartnerReminderPlanner.Planned> planned = plan(link("Babe"), owner(LAST_PERIOD), FIVE_DAYS_BEFORE, 6, 0);

        assertThat(planned).isPresent();
        assertThat(planned.get().type()).isEqualTo(ReminderType.partner_period_upcoming);
        assertThat(planned.get().body()).isEqualTo("Babe's period is expected to start in 5 days.");
        assertThat(planned.get().periodDate()).isEqualTo(NEXT_PERIOD);
    }

    @Test
    void itIsNotSentOnOtherDays() {
        assertThat(plan(link("Babe"), owner(LAST_PERIOD), FIVE_DAYS_BEFORE.plusDays(1), 6, 0)).isEmpty();
        assertThat(plan(link("Babe"), owner(LAST_PERIOD), FIVE_DAYS_BEFORE.minusDays(1), 6, 0)).isEmpty();
    }

    @Test
    void itFollowsTheMorningTimeAndCatchUpWindow() {
        assertThat(plan(link("Babe"), owner(LAST_PERIOD), FIVE_DAYS_BEFORE, 5, 59)).isEmpty();
        assertThat(plan(link("Babe"), owner(LAST_PERIOD), FIVE_DAYS_BEFORE, 7, 45)).isPresent();
        assertThat(plan(link("Babe"), owner(LAST_PERIOD), FIVE_DAYS_BEFORE, 8, 0)).isEmpty();
    }

    @Test
    void itIsSentOnlyOncePerPredictedPeriod() {
        PartnerLink link = link("Babe");
        link.setLastRemindedPeriodDate(NEXT_PERIOD);

        assertThat(plan(link, owner(LAST_PERIOD), FIVE_DAYS_BEFORE, 6, 0)).isEmpty();
    }

    @Test
    void itIsNotSentWhenHerPeriodIsDueOrLate() {
        assertThat(plan(link("Babe"), owner(LocalDate.of(2026, 9, 1)), FIVE_DAYS_BEFORE, 6, 0)).isEmpty();
    }

    @Test
    void itIsNotSentBeforeSheHasLoggedAPeriod() {
        assertThat(plan(link("Babe"), owner(null), FIVE_DAYS_BEFORE, 6, 0)).isEmpty();
    }

    @Test
    void withoutASavedNameItUsesHerFirstName() {
        assertThat(PartnerReminderPlanner.body(link(" "), owner(LAST_PERIOD))).isEqualTo("Amaka's period is expected to start in 5 days.");
    }
}
