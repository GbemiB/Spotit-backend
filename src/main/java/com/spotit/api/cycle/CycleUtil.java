package com.spotit.api.cycle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CycleUtil {
    private CycleUtil() {
    }

    public static final String TRACKING = "tracking";
    public static final String DUE = "due";
    public static final String LATE = "late";

    public record Phase(CyclePhase key, String code) {
    }

    public record Status(String state, int cycleDay, int daysLate, LocalDate expectedDate, int missedCycles) {
        public boolean tracking() {
            return TRACKING.equals(state);
        }
    }

    public static int cycleDayOf(LocalDate date, LocalDate lastPeriodDate, int cycleLength) {
        long diff = ChronoUnit.DAYS.between(lastPeriodDate, date);
        return (int) (Math.floorMod(diff, cycleLength) + 1);
    }

    public static Phase phaseFor(int cycleDay, int periodLength, int cycleLength) {
        int ovDay = cycleLength - 14;
        if (cycleDay <= periodLength) {
            return new Phase(CyclePhase.period, CyclePhase.period.getCode());
        }
        if (cycleDay == ovDay) {
            return new Phase(CyclePhase.ovulation, CyclePhase.ovulation.getCode());
        }
        if (cycleDay >= ovDay - 5 && cycleDay < ovDay) {
            return new Phase(CyclePhase.fertile, CyclePhase.fertile.getCode());
        }
        return null;
    }

    public static Status statusOf(LocalDate date, LocalDate lastPeriodDate, int cycleLength) {
        long diff = ChronoUnit.DAYS.between(lastPeriodDate, date);
        if (diff < cycleLength) {
            return new Status(TRACKING, cycleDayOf(date, lastPeriodDate, cycleLength), 0, lastPeriodDate.plusDays(cycleLength), 0);
        }
        long cycles = diff / cycleLength;
        int daysLate = (int) (diff - cycles * cycleLength);
        return new Status(daysLate == 0 ? DUE : LATE, cycleLength, daysLate, lastPeriodDate.plusDays(cycles * cycleLength), (int) cycles - 1);
    }

    public static LocalDate trackingStartOf(LocalDate storedStart, LocalDate lastPeriodDate) {
        if (storedStart != null) {
            return storedStart;
        }
        return lastPeriodDate == null ? null : lastPeriodDate.withDayOfMonth(1);
    }

    public static LocalDate earlierStart(LocalDate current, LocalDate periodStart) {
        LocalDate candidate = periodStart.withDayOfMonth(1);
        return current == null || candidate.isBefore(current) ? candidate : current;
    }

    public static LocalDate nextPeriodDate(LocalDate today, LocalDate lastPeriodDate, int cycleLength) {
        int cycleDay = cycleDayOf(today, lastPeriodDate, cycleLength);
        int daysLeft = cycleLength - cycleDay + 1;
        return today.plusDays(daysLeft);
    }
}
