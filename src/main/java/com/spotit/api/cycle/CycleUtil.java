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

    /**
     * Null outside period/fertile/ovulation — the luteal and follicular stretches aren't tracked.
     * Fertile window is the 5 days immediately before ovulation (sperm survive up to 5 days; the
     * egg itself is only viable ~24h on the ovulation day, which gets its own distinct label) —
     * matches spotit-mobile's cycle.js and the app's own "5 days before ovulation through the
     * day of ovulation itself" educational copy.
     */
    public static Phase phaseFor(int cycleDay, int periodLength, int cycleLength) {
        int ovDay = cycleLength - 14;
        if (cycleDay <= periodLength) return new Phase(CyclePhase.period, CyclePhase.period.getCode());
        if (cycleDay == ovDay) return new Phase(CyclePhase.ovulation, CyclePhase.ovulation.getCode());
        if (cycleDay >= ovDay - 5 && cycleDay < ovDay) return new Phase(CyclePhase.fertile, CyclePhase.fertile.getCode());
        return null;
    }

    public static LocalDate nextPeriodDate(LocalDate today, LocalDate lastPeriodDate, int cycleLength) {
        int cycleDay = cycleDayOf(today, lastPeriodDate, cycleLength);
        int daysLeft = cycleLength - cycleDay + 1;
        return today.plusDays(daysLeft);
    }
}
