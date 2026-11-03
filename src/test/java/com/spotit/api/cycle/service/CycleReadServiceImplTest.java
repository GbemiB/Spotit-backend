package com.spotit.api.cycle.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CycleReadServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks CycleReadServiceImpl service;

    UUID userId = UUID.randomUUID();
    static final LocalDate TODAY = LocalDate.of(2026, 10, 17);

    private void stubUser(LocalDate lastPeriodDate) {
        stubUser(lastPeriodDate, null);
    }

    private void stubUser(LocalDate lastPeriodDate, LocalDate trackingStart) {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).cycleLength(28).periodLength(5)
                .lastPeriodDate(lastPeriodDate).trackingStartDate(trackingStart).build()));
    }

    private void stubConfidence() {
        when(cycleLogRepository.countByUserId(userId)).thenReturn(0L);
        when(configurationDomainService.getCycleHighConfidenceLogThreshold()).thenReturn(10L);
    }

    private void stubPeriodLogs(LocalDate... days) {
        when(cycleLogRepository.findByUserIdAndFlowIsNotNull(userId)).thenReturn(Arrays.stream(days)
                .map(d -> CycleLog.builder().userId(userId).logDate(d).flow(FlowIntensity.medium).build()).toList());
    }

    private CycleCalendarResponse.DayPhase day(CycleCalendarResponse month, int dayOfMonth) {
        return month.days().get(dayOfMonth - 1);
    }

    @Test
    void currentOnDayOneIsThePeriodPhase() {
        stubUser(TODAY);
        when(cycleLogRepository.countByUserId(userId)).thenReturn(2L);
        when(configurationDomainService.getCycleHighConfidenceLogThreshold()).thenReturn(10L);

        CycleCurrentResponse response = service.getCurrent(userId, TODAY);

        assertThat(response.cycleDay()).isEqualTo(1);
        assertThat(response.phase()).isEqualTo("period");
        assertThat(response.status()).isEqualTo("tracking");
        assertThat(response.nextPeriodDate()).isEqualTo(TODAY.plusDays(28));
        assertThat(response.daysUntilNextPeriod()).isEqualTo(28);
        assertThat(response.confidence()).isEqualTo("estimated");
        assertThat(response.trackingStartDate()).isEqualTo(LocalDate.of(2026, 10, 1));
    }

    @Test
    void currentOnOvulationDayWithEnoughLogsIsHighConfidence() {
        stubUser(TODAY.minusDays(13));
        when(cycleLogRepository.countByUserId(userId)).thenReturn(10L);
        when(configurationDomainService.getCycleHighConfidenceLogThreshold()).thenReturn(10L);

        CycleCurrentResponse response = service.getCurrent(userId, TODAY);

        assertThat(response.cycleDay()).isEqualTo(14);
        assertThat(response.phase()).isEqualTo("ovulation");
        assertThat(response.confidence()).isEqualTo("high");
    }

    @Test
    void currentOutsideAnyTrackedPhaseHasNoPhase() {
        stubUser(TODAY.minusDays(20));
        stubConfidence();

        assertThat(service.getCurrent(userId, TODAY).phase()).isNull();
    }

    @Test
    void onThePredictedDayWithoutALoggedStartThePeriodIsDue() {
        stubUser(TODAY.minusDays(28));
        stubConfidence();

        CycleCurrentResponse response = service.getCurrent(userId, TODAY);

        assertThat(response.status()).isEqualTo("due");
        assertThat(response.daysLate()).isZero();
        assertThat(response.expectedDate()).isEqualTo(TODAY);
        assertThat(response.phase()).isNull();
        assertThat(response.cycleDay()).isEqualTo(28);
        assertThat(response.daysUntilNextPeriod()).isZero();
        assertThat(response.missedCycles()).isZero();
    }

    @Test
    void afterThePredictedDayThePeriodIsLateAndTheCycleDoesNotRestart() {
        stubUser(TODAY.minusDays(30));
        stubConfidence();

        CycleCurrentResponse response = service.getCurrent(userId, TODAY);

        assertThat(response.status()).isEqualTo("late");
        assertThat(response.daysLate()).isEqualTo(2);
        assertThat(response.expectedDate()).isEqualTo(TODAY.minusDays(2));
        assertThat(response.cycleDay()).isEqualTo(28);
        assertThat(response.phase()).isNull();
    }

    @Test
    void lateCountsUntilAFullCycleHasPassedThenRollsOver() {
        stubUser(TODAY.minusDays(55));
        stubConfidence();
        CycleCurrentResponse lastLateDay = service.getCurrent(userId, TODAY);
        assertThat(lastLateDay.status()).isEqualTo("late");
        assertThat(lastLateDay.daysLate()).isEqualTo(27);

        CycleCurrentResponse rolledOver = service.getCurrent(userId, TODAY.plusDays(1));
        assertThat(rolledOver.status()).isEqualTo("due");
        assertThat(rolledOver.expectedDate()).isEqualTo(TODAY.plusDays(1));
        assertThat(rolledOver.missedCycles()).isEqualTo(1);
    }

    @Test
    void aStoredTrackingStartIsReturnedAsIs() {
        stubUser(LocalDate.of(2026, 10, 4), LocalDate.of(2026, 8, 1));
        stubConfidence();

        assertThat(service.getCurrent(userId, TODAY).trackingStartDate()).isEqualTo(LocalDate.of(2026, 8, 1));
    }

    @Test
    void currentWithoutALastPeriodDateReportsInsufficientData() {
        stubUser(null);

        assertThat(service.getCurrent(userId, TODAY)).isEqualTo(CycleCurrentResponse.insufficientData());
        verifyNoInteractions(cycleLogRepository);
    }

    @Test
    void currentForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCurrent(userId, TODAY))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void calendarShowsTheConfirmedCycleAndPredictsFromTodayOn() {
        stubUser(LocalDate.of(2026, 7, 1));

        CycleCalendarResponse july = service.getCalendarMonth(userId, 2026, 7, LocalDate.of(2026, 7, 10));

        assertThat(july.days()).hasSize(31);
        assertThat(day(july, 1)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-07-01", "period", false, null));
        assertThat(day(july, 5).phase()).isEqualTo("period");
        assertThat(day(july, 6).phase()).isNull();
        assertThat(day(july, 9).phase()).isEqualTo("fertile");
        assertThat(day(july, 14).phase()).isEqualTo("ovulation");
        assertThat(day(july, 29)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-07-29", "period", true, null));
    }

    @Test
    void calendarShowsNothingBeforeTheMonthOfTheLastPeriod() {
        stubUser(LocalDate.of(2026, 7, 3), LocalDate.of(2026, 7, 1));

        CycleCalendarResponse june = service.getCalendarMonth(userId, 2026, 6, LocalDate.of(2026, 7, 10));
        CycleCalendarResponse july = service.getCalendarMonth(userId, 2026, 7, LocalDate.of(2026, 7, 10));

        assertThat(june.days()).allSatisfy(d -> {
            assertThat(d.phase()).isNull();
            assertThat(d.marker()).isNull();
        });
        assertThat(day(july, 2).phase()).isNull();
        assertThat(day(july, 3).phase()).isEqualTo("period");
    }

    @Test
    void calendarWithoutAStoredStartUsesTheMonthOfTheLastPeriod() {
        stubUser(LocalDate.of(2026, 7, 20));

        CycleCalendarResponse june = service.getCalendarMonth(userId, 2026, 6, LocalDate.of(2026, 7, 21));

        assertThat(june.days()).allSatisfy(d -> assertThat(d.phase()).isNull());
    }

    @Test
    void anUnconfirmedPredictedDayInThePastIsLeftBlankAndMarked() {
        stubUser(LocalDate.of(2026, 7, 1));
        LocalDate today = LocalDate.of(2026, 8, 3);

        CycleCalendarResponse july = service.getCalendarMonth(userId, 2026, 7, today);
        CycleCalendarResponse august = service.getCalendarMonth(userId, 2026, 8, today);

        assertThat(day(july, 29)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-07-29", null, false, "expected"));
        assertThat(day(july, 30).phase()).isNull();
        assertThat(day(august, 2).phase()).isNull();
        assertThat(day(august, 26)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-08-26", "period", true, null));
    }

    @Test
    void aPredictedStartIsMarkedMissedOnceAFullCycleHasPassed() {
        stubUser(LocalDate.of(2026, 7, 1));
        LocalDate today = LocalDate.of(2026, 8, 27);

        CycleCalendarResponse july = service.getCalendarMonth(userId, 2026, 7, today);
        CycleCalendarResponse august = service.getCalendarMonth(userId, 2026, 8, today);

        assertThat(day(july, 29).marker()).isEqualTo("missed");
        assertThat(day(august, 26).marker()).isEqualTo("expected");
    }

    @Test
    void aDueDayIsAPredictedPeriodMarkedAsExpected() {
        stubUser(LocalDate.of(2026, 7, 1));

        CycleCalendarResponse july = service.getCalendarMonth(userId, 2026, 7, LocalDate.of(2026, 7, 29));

        assertThat(day(july, 29)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-07-29", "period", true, "expected"));
    }

    @Test
    void loggedPeriodDaysReplaceTheEstimatedOnes() {
        stubUser(LocalDate.of(2026, 7, 2));
        stubPeriodLogs(LocalDate.of(2026, 7, 2), LocalDate.of(2026, 7, 3), LocalDate.of(2026, 7, 4));

        CycleCalendarResponse july = service.getCalendarMonth(userId, 2026, 7, LocalDate.of(2026, 7, 10));

        assertThat(day(july, 4)).isEqualTo(new CycleCalendarResponse.DayPhase("2026-07-04", "period", false, null));
        assertThat(day(july, 5).phase()).isNull();
        assertThat(day(july, 6).phase()).isNull();
    }

    @Test
    void calendarWithoutALastPeriodDateHasNoPhases() {
        stubUser(null);

        CycleCalendarResponse response = service.getCalendarMonth(userId, 2026, 2, TODAY);

        assertThat(response.days()).hasSize(28).allSatisfy(d -> assertThat(d.phase()).isNull());
    }

    @Test
    void calendarRejectsAnInvalidMonth() {
        assertThatThrownBy(() -> service.getCalendarMonth(userId, 2026, 13))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.getCalendarMonth(userId, 2026, 0)).isInstanceOf(ApiException.class);
        verifyNoInteractions(userRepository);
    }
}
