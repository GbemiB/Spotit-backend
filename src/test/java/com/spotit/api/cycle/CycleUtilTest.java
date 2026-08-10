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
        // floorMod, not %, so a negative diff still lands on a valid 1..cycleLength day.
        assertThat(CycleUtil.cycleDayOf(LAST_PERIOD.minusDays(1), LAST_PERIOD, 28)).isEqualTo(28);
    }

    @Test
    void phaseCoversTheFullCycleWithNoGaps() {
        int cycleLength = 28;
        int periodLength = 5;
        // ovDay = 28 - 14 = 14
        assertThat(phase(1, periodLength, cycleLength)).isEqualTo(CyclePhase.period);
        assertThat(phase(5, periodLength, cycleLength)).isEqualTo(CyclePhase.period);
        assertThat(phase(6, periodLength, cycleLength)).isEqualTo(CyclePhase.follicular);
        assertThat(phase(9, periodLength, cycleLength)).isEqualTo(CyclePhase.follicular);
        assertThat(phase(10, periodLength, cycleLength)).isEqualTo(CyclePhase.fertile);
        assertThat(phase(13, periodLength, cycleLength)).isEqualTo(CyclePhase.fertile);
        assertThat(phase(14, periodLength, cycleLength)).isEqualTo(CyclePhase.ovulation);
        assertThat(phase(15, periodLength, cycleLength)).isEqualTo(CyclePhase.luteal);
        assertThat(phase(28, periodLength, cycleLength)).isEqualTo(CyclePhase.luteal);
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
        LocalDate today = LAST_PERIOD.plusDays(27); // cycle day 28 of 28
        assertThat(CycleUtil.nextPeriodDate(today, LAST_PERIOD, 28)).isEqualTo(today.plusDays(1));
    }

    private CyclePhase phase(int cycleDay, int periodLength, int cycleLength) {
        return CycleUtil.phaseFor(cycleDay, periodLength, cycleLength).key();
    }
}
