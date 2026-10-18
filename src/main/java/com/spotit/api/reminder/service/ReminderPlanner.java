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

@Component
@RequiredArgsConstructor
public class ReminderPlanner {
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

        boolean confirmedCycle = hasCycle && CycleUtil.statusOf(today, user.getLastPeriodDate(), user.getCycleLength()).tracking();

        if (hasCycle && due(settings.morningTime(), now, settings)) {
            int cycleDay = CycleUtil.cycleDayOf(today, user.getLastPeriodDate(), user.getCycleLength());
            if (user.isNotifPeriod()) {
                period(user, today, cycleDay, NotificationPrefsMapper.periodDaysBefore(user, settings)).ifPresent(planned::add);
            }
            if (user.isNotifOvulation() && confirmedCycle) {
                ovulation(user, cycleDay).ifPresent(planned::add);
            }
        }
        if (confirmedCycle && user.isNotifDailyLog() && due(settings.eveningTime(), now, settings)) {
            logNudge(user, today).ifPresent(planned::add);
        }
        if (user.isNotifDigest() && today.getDayOfWeek() == settings.digestDay() && due(settings.digestTime(), now, settings)) {
            digest(user, today).ifPresent(planned::add);
        }
        return planned;
    }

    private static boolean due(LocalTime target, LocalTime now, ReminderSettings settings) {
        return !now.isBefore(target) && Duration.between(target, now).toMinutes() < settings.catchUpMinutes();
    }

    private static Optional<Planned> period(User user, LocalDate today, int cycleDay, List<Integer> daysBefore) {
        if (cycleDay == 1) {
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

    private static Optional<Planned> ovulation(User user, int cycleDay) {
        int ovulationDay = user.getCycleLength() - LUTEAL_DAYS;
        return ovulationDay >= 1 && cycleDay == ovulationDay
                ? Optional.of(new Planned(ReminderType.ovulation_today, "Today is your predicted ovulation day."))
                : Optional.empty();
    }

    private static String inDays(int days) {
        return days == 1 ? "tomorrow" : "in " + days + " days";
    }

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

    private static boolean hasDetail(CycleLog log) {
        return log.getMood() != null
                || (log.getSymptoms() != null && !log.getSymptoms().isEmpty())
                || (log.getNotes() != null && !log.getNotes().isBlank());
    }

    private Optional<Planned> digest(User user, LocalDate today) {
        List<CycleLog> logs = cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(
                user.getId(), today.minusDays(DIGEST_DAYS - 1L), today);
        if (logs.isEmpty()) {
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
