package com.spotit.api.reminder.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.configuration.ReminderSettingsFixture;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.service.ReminderPlanner.Planned;
import com.spotit.api.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderPlannerTest {
    static final LocalDate LAST_PERIOD = LocalDate.of(2026, 10, 5);
    static final LocalDate OVULATION = LocalDate.of(2026, 10, 18);
    static final LocalDate NEXT_PERIOD = LocalDate.of(2026, 11, 2);
    static final LocalTime MORNING = LocalTime.of(6, 5);
    static final LocalTime EVENING = LocalTime.of(20, 5);

    @Mock CycleLogRepository cycleLogRepository;
    @InjectMocks ReminderPlanner planner;

    ReminderSettings settings = ReminderSettingsFixture.defaults();
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(UUID.randomUUID()).cycleLength(28).periodLength(5).lastPeriodDate(LAST_PERIOD)
                .notifPeriod(true).notifOvulation(true).notifDailyLog(true).notifDigest(true).build();
        lenient().when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        lenient().when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(any(), any(), any())).thenReturn(List.of());
    }

    private List<Planned> plan(LocalDate date, LocalTime time) {
        return planner.plan(user, settings, LocalDateTime.of(date, time));
    }

    private static Planned period(String body) {
        return new Planned(ReminderType.period_upcoming, body);
    }

    @Test
    void thePeriodCountsDownThreeTwoOneDaysAndOnTheDay() {
        assertThat(plan(NEXT_PERIOD.minusDays(4), MORNING)).isEmpty();
        assertThat(plan(NEXT_PERIOD.minusDays(3), MORNING)).containsExactly(period("Your period is predicted to start in 3 days."));
        assertThat(plan(NEXT_PERIOD.minusDays(2), MORNING)).containsExactly(period("Your period is predicted to start in 2 days."));
        assertThat(plan(NEXT_PERIOD.minusDays(1), MORNING)).containsExactly(period("Your period is predicted to start tomorrow."));
        assertThat(plan(NEXT_PERIOD, MORNING))
                .containsExactly(new Planned(ReminderType.period_today, "Your period may start today. Tap to log it."));
    }

    @Test
    void ovulationIsAnnouncedOnTheDayOnlyWithNoCountdown() {
        assertThat(plan(OVULATION.minusDays(3), MORNING)).isEmpty();
        assertThat(plan(OVULATION.minusDays(2), MORNING)).isEmpty();
        assertThat(plan(OVULATION.minusDays(1), MORNING)).isEmpty();
        assertThat(plan(OVULATION, MORNING))
                .containsExactly(new Planned(ReminderType.ovulation_today, "Today is your predicted ovulation day."));
        assertThat(plan(OVULATION.plusDays(1), MORNING)).isEmpty();
    }

    @Test
    void shortAndLongCyclesMoveTheDates() {
        user.setCycleLength(21);
        assertThat(plan(LocalDate.of(2026, 10, 23), MORNING)).containsExactly(period("Your period is predicted to start in 3 days."));
        assertThat(plan(LocalDate.of(2026, 10, 11), MORNING)).extracting(Planned::type).containsExactly(ReminderType.ovulation_today);

        user.setCycleLength(35);
        assertThat(plan(LocalDate.of(2026, 11, 8), MORNING)).containsExactly(period("Your period is predicted to start tomorrow."));
        assertThat(plan(LocalDate.of(2026, 10, 25), MORNING)).extracting(Planned::type).containsExactly(ReminderType.ovulation_today);
    }

    @Test
    void turningPeriodOffLeavesOvulationAlone() {
        user.setNotifPeriod(false);

        assertThat(plan(NEXT_PERIOD.minusDays(3), MORNING)).isEmpty();
        assertThat(plan(OVULATION, MORNING)).extracting(Planned::type).containsExactly(ReminderType.ovulation_today);
    }

    @Test
    void turningOvulationOffLeavesThePeriodAlone() {
        user.setNotifOvulation(false);

        assertThat(plan(OVULATION, MORNING)).isEmpty();
        assertThat(plan(NEXT_PERIOD, MORNING)).extracting(Planned::type).containsExactly(ReminderType.period_today);
    }

    @Test
    void aUserOnlyGetsTheCountdownDaysSheKept() {
        user.setPeriodDaysBefore("1,0");

        assertThat(plan(NEXT_PERIOD.minusDays(3), MORNING)).isEmpty();
        assertThat(plan(NEXT_PERIOD.minusDays(2), MORNING)).isEmpty();
        assertThat(plan(NEXT_PERIOD.minusDays(1), MORNING)).hasSize(1);
        assertThat(plan(NEXT_PERIOD, MORNING)).hasSize(1);
        assertThat(plan(OVULATION.minusDays(1), MORNING)).isEmpty();
        assertThat(plan(OVULATION, MORNING)).hasSize(1);
    }

    @Test
    void theServerDefaultCountdownAppliesToUsersWhoHaveNotChosen() {
        settings = ReminderSettingsFixture.withPeriodDays(List.of(2));

        assertThat(plan(NEXT_PERIOD.minusDays(3), MORNING)).isEmpty();
        assertThat(plan(NEXT_PERIOD.minusDays(2), MORNING)).hasSize(1);
        assertThat(plan(NEXT_PERIOD, MORNING)).isEmpty();

        user.setPeriodDaysBefore("3");
        assertThat(plan(NEXT_PERIOD.minusDays(3), MORNING)).hasSize(1);
        assertThat(plan(NEXT_PERIOD.minusDays(2), MORNING)).isEmpty();
    }

    @Test
    void aPeriodLoggedEarlyCancelsTheRestOfTheCountdown() {
        LocalDate early = NEXT_PERIOD.minusDays(2);
        user.setLastPeriodDate(early);

        assertThat(plan(early, MORNING)).isEmpty();
        assertThat(plan(early.plusDays(1), MORNING)).isEmpty();
        assertThat(plan(NEXT_PERIOD, MORNING)).isEmpty();
    }

    @Test
    void aPeriodLoggedOnThePredictedDayIsNotAnnouncedAsAPrediction() {
        user.setLastPeriodDate(NEXT_PERIOD);

        assertThat(plan(NEXT_PERIOD, MORNING)).isEmpty();
    }

    @Test
    void remindersAreDueFromTheirTimeUntilTheCatchUpWindowCloses() {
        LocalDate day = NEXT_PERIOD.minusDays(3);

        assertThat(plan(day, LocalTime.of(5, 59))).isEmpty();
        assertThat(plan(day, LocalTime.of(6, 0))).hasSize(1);
        assertThat(plan(day, LocalTime.of(7, 59))).hasSize(1);
        assertThat(plan(day, LocalTime.of(8, 0))).isEmpty();
    }

    @Test
    void theLogNudgeGoesOutOnPeriodDaysOnly() {
        user.setNotifDigest(false);

        assertThat(plan(LAST_PERIOD.plusDays(1), EVENING))
                .containsExactly(new Planned(ReminderType.log_nudge, "How are you feeling today? Add today's log."));
        assertThat(plan(LAST_PERIOD.plusDays(4), EVENING)).hasSize(1);
        assertThat(plan(LAST_PERIOD.plusDays(5), EVENING)).isEmpty();
        assertThat(plan(LAST_PERIOD.plusDays(1), LocalTime.of(19, 59))).isEmpty();
    }

    @Test
    void theLogNudgeIsSkippedOnceTheDayHasDetail() {
        user.setNotifDigest(false);
        LocalDate day = LAST_PERIOD.plusDays(1);

        when(cycleLogRepository.findByUserIdAndLogDate(user.getId(), day))
                .thenReturn(Optional.of(CycleLog.builder().flow(FlowIntensity.medium).build()));
        assertThat(plan(day, EVENING)).hasSize(1);

        when(cycleLogRepository.findByUserIdAndLogDate(user.getId(), day))
                .thenReturn(Optional.of(CycleLog.builder().mood(MoodType.calm).build()));
        assertThat(plan(day, EVENING)).isEmpty();

        when(cycleLogRepository.findByUserIdAndLogDate(user.getId(), day))
                .thenReturn(Optional.of(CycleLog.builder().symptoms(List.of(1)).build()));
        assertThat(plan(day, EVENING)).isEmpty();

        when(cycleLogRepository.findByUserIdAndLogDate(user.getId(), day))
                .thenReturn(Optional.of(CycleLog.builder().notes("cramps").build()));
        assertThat(plan(day, EVENING)).isEmpty();
    }

    @Test
    void theLogNudgeSwitchIsIndependent() {
        user.setNotifDailyLog(false);

        assertThat(plan(LAST_PERIOD.plusDays(1), EVENING)).isEmpty();
    }

    @Test
    void theWeeklyDigestCarriesRealFigures() {
        LocalDate sunday = LocalDate.of(2026, 10, 11);
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(eq(user.getId()), eq(sunday.minusDays(6)), eq(sunday)))
                .thenReturn(List.of(
                        CycleLog.builder().mood(MoodType.calm).build(),
                        CycleLog.builder().mood(MoodType.sad).build(),
                        CycleLog.builder().mood(MoodType.calm).build(),
                        CycleLog.builder().build()));

        assertThat(plan(sunday, LocalTime.of(19, 0))).contains(
                new Planned(ReminderType.weekly_digest, "You logged 4 of 7 days this week. Your most frequent mood was calm."));
    }

    @Test
    void theWeeklyDigestLeavesTheMoodOutWhenNoneWasLogged() {
        LocalDate sunday = LocalDate.of(2026, 10, 25);
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(any(), any(), any()))
                .thenReturn(List.of(CycleLog.builder().build()));

        assertThat(plan(sunday, LocalTime.of(19, 0)))
                .containsExactly(new Planned(ReminderType.weekly_digest, "You logged 1 of 7 days this week."));
    }

    @Test
    void theWeeklyDigestIsOnlySentOnItsDayAtItsTimeWhenThereIsSomethingToSay() {
        LocalDate sunday = LocalDate.of(2026, 10, 25);

        assertThat(plan(sunday, LocalTime.of(19, 0))).isEmpty();

        lenient().when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(any(), any(), any()))
                .thenReturn(List.of(CycleLog.builder().build()));
        assertThat(plan(sunday.minusDays(1), LocalTime.of(19, 0))).isEmpty();
        assertThat(plan(sunday, LocalTime.of(18, 59))).isEmpty();

        user.setNotifDigest(false);
        assertThat(plan(sunday, LocalTime.of(19, 0))).isEmpty();
    }

    @Test
    void aUserWithNoCycleDataGetsNoCycleReminders() {
        user.setLastPeriodDate(null);
        user.setNotifDigest(false);

        assertThat(plan(NEXT_PERIOD, MORNING)).isEmpty();
        assertThat(plan(LAST_PERIOD, EVENING)).isEmpty();
    }

    @Test
    void anAccountNotOpenedForMonthsKeepsGettingCorrectDates() {
        LocalDate later = NEXT_PERIOD.plusDays(28 * 3);

        assertThat(plan(later.minusDays(3), MORNING)).containsExactly(period("Your period is predicted to start in 3 days."));
        assertThat(plan(later, MORNING)).extracting(Planned::type).containsExactly(ReminderType.period_today);
    }

    @Test
    void uncheckingOnTheDayStopsOnlyThePeriodTodayReminder() {
        user.setPeriodDaysBefore("3,2,1");

        assertThat(plan(NEXT_PERIOD.minusDays(1), MORNING)).contains(period("Your period is predicted to start tomorrow."));
        assertThat(plan(NEXT_PERIOD, MORNING)).isEmpty();
    }

    @Test
    void theDayAfterThePeriodStartsIsNotACountdownDay() {
        user.setNotifOvulation(false);

        for (int day = 1; day <= 24; day++) {
            assertThat(plan(LAST_PERIOD.plusDays(day), MORNING)).as("cycle day %d", day + 1).isEmpty();
        }
    }

    @Test
    void ovulationTodayHasItsOwnTypeAndWording() {
        user.setNotifPeriod(false);

        assertThat(plan(OVULATION, MORNING))
                .containsExactly(new Planned(ReminderType.ovulation_today, "Today is your predicted ovulation day."));
        assertThat(plan(OVULATION.plusDays(1), MORNING)).isEmpty();
    }

    @Test
    void aCycleTooShortToEstimateOvulationStillGetsPeriodReminders() {
        user.setCycleLength(14);
        LocalDate next = LAST_PERIOD.plusDays(14);

        for (int day = 0; day < 14; day++) {
            assertThat(plan(LAST_PERIOD.plusDays(day), MORNING)).extracting(Planned::type)
                    .doesNotContain(ReminderType.ovulation_upcoming, ReminderType.ovulation_today);
        }
        assertThat(plan(next.minusDays(3), MORNING)).containsExactly(period("Your period is predicted to start in 3 days."));
    }

    @Test
    void noOvulationReminderWhileThePeriodIsDueOrLate() {
        user.setCycleLength(15);
        LocalDate next = LAST_PERIOD.plusDays(15);

        assertThat(plan(next, MORNING)).containsExactly(
                new Planned(ReminderType.period_today, "Your period may start today. Tap to log it."));
        assertThat(plan(OVULATION.plusDays(28), MORNING)).extracting(Planned::type).doesNotContain(ReminderType.ovulation_today);
    }

    @Test
    void noLogNudgeOnPredictedPeriodDaysThatWereNotConfirmed() {
        user.setNotifDigest(false);

        assertThat(plan(NEXT_PERIOD.plusDays(1), EVENING)).isEmpty();
    }

    @Test
    void theMorningAndEveningRemindersDoNotLeakIntoEachOther() {
        user.setNotifDigest(false);

        assertThat(plan(NEXT_PERIOD.minusDays(3), EVENING)).isEmpty();
        assertThat(plan(LAST_PERIOD.plusDays(1), MORNING)).isEmpty();
    }

    @Test
    void aMoodTieInTheDigestIsSettledTheSameWayEveryTime() {
        LocalDate sunday = LocalDate.of(2026, 10, 25);
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(any(), any(), any())).thenReturn(List.of(
                CycleLog.builder().mood(MoodType.happy).build(),
                CycleLog.builder().mood(MoodType.sad).build()));

        assertThat(plan(sunday, LocalTime.of(19, 0))).containsExactly(
                new Planned(ReminderType.weekly_digest, "You logged 2 of 7 days this week. Your most frequent mood was sad."));
    }

    @Test
    void theDigestDoesNotNeedCycleData() {
        user.setLastPeriodDate(null);
        LocalDate sunday = LocalDate.of(2026, 10, 25);
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(any(), any(), any()))
                .thenReturn(List.of(CycleLog.builder().build()));

        assertThat(plan(sunday, LocalTime.of(19, 0))).extracting(Planned::type).containsExactly(ReminderType.weekly_digest);
    }
}
