package com.spotit.api.cycle;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CycleUtilTest {
    private static final LocalDate LAST_PERIOD = LocalDate.of(2026, 1, 1);

    @Test
    void cycleDayOnPeriodStartIsDayOne() {
        assertThat(CycleUtil.cycleDayOf(LAST_PERIOD, LAST_PERIOD, 28)).isEqualTo(1);
    }

    @Test
    void cycleDayAtEndOfCycleIsTheLastDay() {
        assertThat(CycleUtil.cycleDayOf(LAST_PERIOD.plusDays(27), LAST_PERIOD, 28)).isEqualTo(28);
    }

    @Test
    void cycleDayWrapsIntoTheNextCycleAfterCycleLengthDays() {
        assertThat(CycleUtil.cycleDayOf(LAST_PERIOD.plusDays(28), LAST_PERIOD, 28)).isEqualTo(1);
        assertThat(CycleUtil.cycleDayOf(LAST_PERIOD.plusDays(29), LAST_PERIOD, 28)).isEqualTo(2);
    }

    @Test
    void cycleDayForADateBeforeTheLastPeriodWrapsBackwardCorrectly() {
        assertThat(CycleUtil.cycleDayOf(LAST_PERIOD.minusDays(1), LAST_PERIOD, 28)).isEqualTo(28);
    }

    @Test
    void phaseIsNullOutsidePeriodFertileOvulation() {
        int cycleLength = 28;
        int periodLength = 5;

        assertThat(phase(1, periodLength, cycleLength)).isEqualTo(CyclePhase.period);
        assertThat(phase(5, periodLength, cycleLength)).isEqualTo(CyclePhase.period);
        assertThat(phase(6, periodLength, cycleLength)).isNull();
        assertThat(phase(8, periodLength, cycleLength)).isNull();
        assertThat(phase(9, periodLength, cycleLength)).isEqualTo(CyclePhase.fertile);
        assertThat(phase(13, periodLength, cycleLength)).isEqualTo(CyclePhase.fertile);
        assertThat(phase(14, periodLength, cycleLength)).isEqualTo(CyclePhase.ovulation);
        assertThat(phase(15, periodLength, cycleLength)).isNull();
        assertThat(phase(28, periodLength, cycleLength)).isNull();
    }

    @Test
    void phaseCodeMatchesTheEnumConstant() {
        CycleUtil.Phase p = CycleUtil.phaseFor(1, 5, 28);
        assertThat(p.key()).isEqualTo(CyclePhase.period);
        assertThat(p.code()).isEqualTo(CyclePhase.period.getCode());
    }

    @Test
    void nextPeriodDateOnDayOneIsAFullCycleAway() {
        LocalDate today = LAST_PERIOD;
        assertThat(CycleUtil.nextPeriodDate(today, LAST_PERIOD, 28)).isEqualTo(LAST_PERIOD.plusDays(28));
    }

    @Test
    void nextPeriodDateOnTheLastCycleDayIsTomorrow() {
        LocalDate today = LAST_PERIOD.plusDays(27);
        assertThat(CycleUtil.nextPeriodDate(today, LAST_PERIOD, 28)).isEqualTo(today.plusDays(1));
    }

    @Test
    void statusIsTrackingWithinTheConfirmedCycle() {
        CycleUtil.Status status = CycleUtil.statusOf(LAST_PERIOD.plusDays(27), LAST_PERIOD, 28);
        assertThat(status.state()).isEqualTo(CycleUtil.TRACKING);
        assertThat(status.cycleDay()).isEqualTo(28);
        assertThat(status.expectedDate()).isEqualTo(LAST_PERIOD.plusDays(28));
    }

    @Test
    void statusIsDueOnThePredictedDayThenLateUntilAFullCycleHasPassed() {
        assertThat(CycleUtil.statusOf(LAST_PERIOD.plusDays(28), LAST_PERIOD, 28))
                .isEqualTo(new CycleUtil.Status(CycleUtil.DUE, 28, 0, LAST_PERIOD.plusDays(28), 0));
        assertThat(CycleUtil.statusOf(LAST_PERIOD.plusDays(29), LAST_PERIOD, 28))
                .isEqualTo(new CycleUtil.Status(CycleUtil.LATE, 28, 1, LAST_PERIOD.plusDays(28), 0));
        assertThat(CycleUtil.statusOf(LAST_PERIOD.plusDays(55), LAST_PERIOD, 28))
                .isEqualTo(new CycleUtil.Status(CycleUtil.LATE, 28, 27, LAST_PERIOD.plusDays(28), 0));
        assertThat(CycleUtil.statusOf(LAST_PERIOD.plusDays(56), LAST_PERIOD, 28))
                .isEqualTo(new CycleUtil.Status(CycleUtil.DUE, 28, 0, LAST_PERIOD.plusDays(56), 1));
    }

    @Test
    void trackingStartFallsBackToTheMonthOfTheLastPeriod() {
        assertThat(CycleUtil.trackingStartOf(null, LocalDate.of(2026, 9, 28))).isEqualTo(LocalDate.of(2026, 9, 1));
        assertThat(CycleUtil.trackingStartOf(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 9, 28))).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(CycleUtil.trackingStartOf(null, null)).isNull();
    }

    @Test
    void earlierStartOnlyEverMovesBack() {
        assertThat(CycleUtil.earlierStart(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 7, 15))).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(CycleUtil.earlierStart(LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 3))).isEqualTo(LocalDate.of(2026, 9, 1));
        assertThat(CycleUtil.earlierStart(null, LocalDate.of(2026, 10, 3))).isEqualTo(LocalDate.of(2026, 10, 1));
    }

    private CyclePhase phase(int cycleDay, int periodLength, int cycleLength) {
        CycleUtil.Phase p = CycleUtil.phaseFor(cycleDay, periodLength, cycleLength);
        return p == null ? null : p.key();
    }
}
