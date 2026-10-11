package com.spotit.api.reminder.service;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.service.NotificationPrefsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Decides which reminders one user is due at a given moment of her local time. It only decides: whether a reminder
 * was already sent today is the delivery record's job, so calling this every 15 minutes is safe.
 *
 * <p>Cycle dates come from {@link CycleUtil}, the same calculation the app's calendar uses, so a reminder never
 * disagrees with what she sees on screen.
 */
@Component
@RequiredArgsConstructor
public class ReminderPlanner {
    // Ovulation is estimated 14 days before the next period, as in CycleUtil.phaseFor.
    private static final int LUTEAL_DAYS = 14;
    private static final int DIGEST_DAYS = 7;

    private final CycleLogRepository cycleLogRepository;

    public record Planned(ReminderType type, String body) {
    }

    public List<Planned> plan(User user, ReminderSettings settings, LocalDateTime localNow) {
        List<Planned> planned = new ArrayList<>();
        LocalDate today = localNow.toLocalDate();
        LocalTime now = localNow.toLocalTime();
        boolean hasCycle = user.getLastPeriodDate() != null && user.getCycleLength() > 0;

        if (hasCycle && due(NotificationPrefsMapper.morningTime(user, settings), now, settings)) {
            int cycleDay = CycleUtil.cycleDayOf(today, user.getLastPeriodDate(), user.getCycleLength());
            if (user.isNotifPeriod()) {
                period(user, today, cycleDay, NotificationPrefsMapper.periodDaysBefore(user, settings)).ifPresent(planned::add);
            }
            if (user.isNotifOvulation()) {
                ovulation(user, cycleDay).ifPresent(planned::add);
            }
        }
        if (hasCycle && user.isNotifDailyLog() && due(NotificationPrefsMapper.eveningTime(user, settings), now, settings)) {
            logNudge(user, today).ifPresent(planned::add);
        }
        if (user.isNotifDigest() && today.getDayOfWeek() == settings.digestDay() && due(settings.digestTime(), now, settings)) {
            digest(user, today).ifPresent(planned::add);
        }
        return planned;
    }

    // Due from its time until the catch-up window closes, so a run missed during a restart is made up, but nothing arrives hours late.
    private static boolean due(LocalTime target, LocalTime now, ReminderSettings settings) {
        return !now.isBefore(target) && Duration.between(target, now).toMinutes() < settings.catchUpMinutes();
    }

    private static Optional<Planned> period(User user, LocalDate today, int cycleDay, List<Integer> daysBefore) {
        if (cycleDay == 1) {
            // Day 1 with the start already logged today is a period in progress, not a prediction.
            boolean predictedToday = user.getLastPeriodDate().isBefore(today) && daysBefore.contains(0);
            return predictedToday
                    ? Optional.of(new Planned(ReminderType.period_today, "Your period may start today. Tap to log it."))
                    : Optional.empty();
        }
        int daysLeft = user.getCycleLength() - cycleDay + 1;
        if (!daysBefore.contains(daysLeft)) {
            return Optional.empty();
        }
        return Optional.of(new Planned(ReminderType.period_upcoming, "Your period is predicted to start " + inDays(daysLeft) + "."));
    }

    // Ovulation is announced on the day only, with no countdown.
    private static Optional<Planned> ovulation(User user, int cycleDay) {
        int ovulationDay = user.getCycleLength() - LUTEAL_DAYS;
        return ovulationDay >= 1 && cycleDay == ovulationDay
                ? Optional.of(new Planned(ReminderType.ovulation_today, "Today is your predicted ovulation day."))
                : Optional.empty();
    }

    private static String inDays(int days) {
        return days == 1 ? "tomorrow" : "in " + days + " days";
    }

    // Mood and symptoms can only be logged on period days for now, so those are the only days worth a nudge.
    // Dropping the period-day condition turns this into a true daily reminder once any day can be logged.
    private Optional<Planned> logNudge(User user, LocalDate today) {
        int cycleDay = CycleUtil.cycleDayOf(today, user.getLastPeriodDate(), user.getCycleLength());
        if (cycleDay > user.getPeriodLength()) {
            return Optional.empty();
        }
        boolean alreadyLogged = cycleLogRepository.findByUserIdAndLogDate(user.getId(), today).map(ReminderPlanner::hasDetail).orElse(false);
        return alreadyLogged
                ? Optional.empty()
                : Optional.of(new Planned(ReminderType.log_nudge, "How are you feeling today? Add today's log."));
    }

    // Logging a period creates a flow-only entry for each of its days; that alone is not "today's log".
    private static boolean hasDetail(CycleLog log) {
        return log.getMood() != null
                || (log.getSymptoms() != null && !log.getSymptoms().isEmpty())
                || (log.getNotes() != null && !log.getNotes().isBlank());
    }

    private Optional<Planned> digest(User user, LocalDate today) {
        List<CycleLog> logs = cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(
                user.getId(), today.minusDays(DIGEST_DAYS - 1L), today);
        if (logs.isEmpty()) {
            // A summary of nothing is only noise.
            return Optional.empty();
        }
        String body = "You logged " + logs.size() + " of " + DIGEST_DAYS + " days this week.";
        Optional<MoodType> topMood = logs.stream().map(CycleLog::getMood).filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.<MoodType, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey);
        if (topMood.isPresent()) {
            body += " Your most frequent mood was " + topMood.get().name() + ".";
        }
        return Optional.of(new Planned(ReminderType.weekly_digest, body));
    }
}
