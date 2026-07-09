package com.spotit.api.cycle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Mirrors spotit-mobile's src/shared/utils/cycle.js exactly (cycleDayOf/phaseFor/nextPeriodDate). */
public final class CycleUtil {

    private CycleUtil() {
    }

    public record Phase(CyclePhase key, String label) {
    }

    public static int cycleDayOf(LocalDate date, LocalDate lastPeriodDate, int cycleLength) {
        long diff = ChronoUnit.DAYS.between(lastPeriodDate, date);
        return (int) (Math.floorMod(diff, cycleLength) + 1);
    }

    public static Phase phaseFor(int cycleDay, int periodLength, int cycleLength) {
        int ovDay = cycleLength - 14;
        if (cycleDay <= periodLength) return new Phase(CyclePhase.period, "Period");
        if (cycleDay == ovDay) return new Phase(CyclePhase.ovulation, "Ovulation");
        if (cycleDay >= ovDay - 4 && cycleDay < ovDay) return new Phase(CyclePhase.fertile, "Fertile");
        if (cycleDay > ovDay) return new Phase(CyclePhase.luteal, "Luteal");
        return new Phase(CyclePhase.follicular, "Follicular");
    }

    public static LocalDate nextPeriodDate(LocalDate today, LocalDate lastPeriodDate, int cycleLength) {
        int cycleDay = cycleDayOf(today, lastPeriodDate, cycleLength);
        int daysLeft = cycleLength - cycleDay + 1;
        return today.plusDays(daysLeft);
    }
}
