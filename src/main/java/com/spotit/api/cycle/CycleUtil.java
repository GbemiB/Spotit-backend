package com.spotit.api.cycle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Mirrors spotit-mobile's src/shared/utils/cycle.js exactly (cycleDayOf/phaseFor/nextPeriodDate). */
public final class CycleUtil {

    private CycleUtil() {
    }

    public record Phase(CyclePhase key, String code) {
    }

    public static int cycleDayOf(LocalDate date, LocalDate lastPeriodDate, int cycleLength) {
        long diff = ChronoUnit.DAYS.between(lastPeriodDate, date);
        return (int) (Math.floorMod(diff, cycleLength) + 1);
    }

    public static Phase phaseFor(int cycleDay, int periodLength, int cycleLength) {
        int ovDay = cycleLength - 14;
        CyclePhase key;
        if (cycleDay <= periodLength) key = CyclePhase.period;
        else if (cycleDay == ovDay) key = CyclePhase.ovulation;
        else if (cycleDay >= ovDay - 4 && cycleDay < ovDay) key = CyclePhase.fertile;
        else if (cycleDay > ovDay) key = CyclePhase.luteal;
        else key = CyclePhase.follicular;
        return new Phase(key, key.getCode());
    }

    public static LocalDate nextPeriodDate(LocalDate today, LocalDate lastPeriodDate, int cycleLength) {
        int cycleDay = cycleDayOf(today, lastPeriodDate, cycleLength);
        int daysLeft = cycleLength - cycleDay + 1;
        return today.plusDays(daysLeft);
    }
}
