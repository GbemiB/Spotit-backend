package com.spotit.api.log.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.cycle.PeriodEpisodes;
import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogPeriodRequest;
import com.spotit.api.log.dto.LogPeriodResponse;
import com.spotit.api.log.dto.RemovePeriodResponse;
import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.entity.SymptomType;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogWriteServiceImpl implements LogWriteService {
    private final CycleLogRepository cycleLogRepository;
    private final PointsWriteService pointsWriteService;
    private final UserRepository userRepository;
    private final ConfigurationDomainService configurationDomainService;
    private final UserClock userClock;

    @Override
    @Transactional
    public SaveLogResponse saveLog(UUID userId, LocalDate date, SaveLogRequest request) {
        var existing = cycleLogRepository.findByUserIdAndLogDate(userId, date);
        boolean isNewEntry = existing.isEmpty();
        CycleLog entry = existing.orElseGet(() -> CycleLog.builder().userId(userId).logDate(date).build());

        entry.setFlow(request.flow() == null ? null : FlowIntensity.valueOf(request.flow()));
        entry.setMood(request.mood() == null ? null : MoodType.valueOf(request.mood()));
        entry.setSymptoms(SymptomType.toValues(request.symptoms()));
        entry.setNotes(request.notes());
        entry.setIntimate(request.intimate());
        cycleLogRepository.save(entry);

        var pointsResult = pointsWriteService.recordDailyLog(userId, date, isNewEntry);

        return new SaveLogResponse(
                date, request.flow(), request.mood(), SymptomType.fromValues(entry.getSymptoms()), entry.getNotes(), entry.isIntimate(),
                pointsResult.pointsAwarded(), pointsResult.newBalance(), pointsResult.streak(), isNewEntry
        );
    }

    @Override
    @Transactional
    public LogPeriodResponse logPeriod(UUID userId, LogPeriodRequest request) {
        LocalDate startDate = request.startDate();
        LocalDate endDate = request.endDate();
        if (endDate.isBefore(startDate)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PERIOD_START_AFTER_END);
        }
        if (ChronoUnit.DAYS.between(startDate, endDate) + 1 > configurationDomainService.getLogMaxPeriodRangeDays()) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PERIOD_RANGE_TOO_LONG);
        }

        LocalDate detailDate = request.detailDate() != null ? request.detailDate() : startDate;
        if (detailDate.isBefore(startDate) || detailDate.isAfter(endDate)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PERIOD_DETAIL_DATE_OUT_OF_RANGE);
        }

        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        if (startDate.isAfter(userClock.today(user))) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PERIOD_START_IN_FUTURE);
        }

        LocalDate replaceStart = request.replaceStart();
        List<CycleLog> replaced = List.of();
        LocalDate otherLatestStart = null;
        if (replaceStart != null) {
            List<CycleLog> flowBefore = sortedFlowDays(userId);
            replaced = periodAround(flowBefore, replaceStart);
            otherLatestStart = latestStartWithout(flowBefore, replaced);
        }
        LocalDate replacedFrom = replaced.isEmpty() ? replaceStart : replaced.get(0).getLogDate();
        LocalDate replacedTo = replaced.isEmpty() ? replaceStart : replaced.get(replaced.size() - 1).getLogDate();

        LogEntryResponse startDayEntry = null;
        long pointsAwarded = 0;
        long newBalance = user.getPoints();
        int streak = user.getStreak();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            var existing = cycleLogRepository.findByUserIdAndLogDate(userId, currentDate);
            CycleLog entry = existing.orElseGet(() -> CycleLog.builder().userId(userId).logDate(currentDate).build());

            boolean isDetailDay = currentDate.equals(detailDate);
            if (isDetailDay) {
                entry.setFlow(request.flow() == null ? null : FlowIntensity.valueOf(request.flow()));
                entry.setMood(request.mood() == null ? null : MoodType.valueOf(request.mood()));
                entry.setSymptoms(SymptomType.toValues(request.symptoms()));
                entry.setNotes(request.notes());
                entry.setIntimate(request.intimate());
            } else {
                entry.setFlow(request.flow() == null ? null : FlowIntensity.valueOf(request.flow()));
            }
            cycleLogRepository.save(entry);

            if (isDetailDay) {
                var pointsResult = pointsWriteService.recordPeriodLog(userId);
                pointsAwarded = pointsResult.pointsAwarded();
                newBalance = pointsResult.newBalance();
                streak = pointsResult.streak();
                startDayEntry = new LogEntryResponse(
                        currentDate, request.flow(), request.mood(), SymptomType.fromValues(entry.getSymptoms()), entry.getNotes(), entry.isIntimate());
            }
        }

        ClearedPeriod cleared = clearRestOfSamePeriod(userId, startDate, endDate);
        List<LogEntryResponse> clearedEntries = new ArrayList<>(cleared.entries());
        for (CycleLog entry : replaced) {
            LocalDate d = entry.getLogDate();
            if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                continue;
            }
            if (entry.getFlow() == null) {
                continue;
            }
            clearedEntries.add(clearFlow(entry));
        }

        LocalDate lastPeriod = user.getLastPeriodDate();
        user.setTrackingStartDate(CycleUtil.earlierStart(CycleUtil.trackingStartOf(user.getTrackingStartDate(), lastPeriod), startDate));
        boolean movedLastPeriod = replaceStart != null && lastPeriod != null && near(lastPeriod, replacedFrom, replacedTo);
        if (movedLastPeriod) {
            user.setLastPeriodDate(otherLatestStart != null && otherLatestStart.isAfter(startDate) ? otherLatestStart : startDate);
        } else if (lastPeriod == null || startDate.isAfter(lastPeriod) || cleared.touches(lastPeriod)) {
            user.setLastPeriodDate(startDate);
        }
        userRepository.save(user);

        return new LogPeriodResponse(
                startDate, endDate, request.flow(), user.getLastPeriodDate(), user.getTrackingStartDate(), user.getCycleLength(), user.getPeriodLength(),
                startDayEntry, pointsAwarded, newBalance, streak, clearedEntries
        );
    }

    @Override
    @Transactional
    public void deleteLog(UUID userId, LocalDate date) {
        cycleLogRepository.deleteByUserIdAndLogDate(userId, date);
    }

    @Override
    @Transactional
    public RemovePeriodResponse removePeriod(UUID userId, LocalDate date) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        List<CycleLog> flowDays = sortedFlowDays(userId);
        List<CycleLog> period = periodAround(flowDays, date);
        if (period.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.PERIOD_NOT_FOUND);
        }
        LocalDate from = period.get(0).getLogDate();
        LocalDate to = period.get(period.size() - 1).getLogDate();
        LocalDate otherLatestStart = latestStartWithout(flowDays, period);

        List<LogEntryResponse> cleared = new ArrayList<>();
        for (CycleLog entry : period) {
            cleared.add(clearFlow(entry));
        }

        LocalDate lastPeriod = user.getLastPeriodDate();
        if (lastPeriod != null && near(lastPeriod, from, to)) {
            user.setLastPeriodDate(otherLatestStart);
            userRepository.save(user);
        }
        return new RemovePeriodResponse(user.getLastPeriodDate(), user.getTrackingStartDate(), cleared);
    }

    private List<CycleLog> sortedFlowDays(UUID userId) {
        return cycleLogRepository.findByUserIdAndFlowIsNotNull(userId).stream()
                .sorted(Comparator.comparing(CycleLog::getLogDate))
                .toList();
    }

    private static List<CycleLog> periodAround(List<CycleLog> sortedFlowDays, LocalDate anchor) {
        List<CycleLog> before = new ArrayList<>();
        LocalDate from = anchor;
        for (int i = sortedFlowDays.size() - 1; i >= 0; i--) {
            LocalDate d = sortedFlowDays.get(i).getLogDate();
            if (d.isAfter(anchor)) {
                continue;
            }
            if (!PeriodEpisodes.sameEpisode(d, from)) {
                break;
            }
            before.add(0, sortedFlowDays.get(i));
            from = d;
        }
        List<CycleLog> period = new ArrayList<>(before);
        LocalDate to = anchor;
        for (CycleLog entry : sortedFlowDays) {
            LocalDate d = entry.getLogDate();
            if (!d.isAfter(anchor)) {
                continue;
            }
            if (!PeriodEpisodes.sameEpisode(to, d)) {
                break;
            }
            period.add(entry);
            to = d;
        }
        return period;
    }

    private static LocalDate latestStartWithout(List<CycleLog> flowDays, List<CycleLog> excluded) {
        List<LocalDate> excludedDates = excluded.stream().map(CycleLog::getLogDate).toList();
        List<CycleLog> rest = flowDays.stream().filter(l -> !excludedDates.contains(l.getLogDate())).toList();
        List<PeriodEpisodes.Episode> episodes = PeriodEpisodes.detect(rest);
        return episodes.isEmpty() ? null : episodes.get(episodes.size() - 1).start();
    }

    private static boolean near(LocalDate date, LocalDate from, LocalDate to) {
        return !date.isBefore(from.minusDays(PeriodEpisodes.MAX_GAP_DAYS)) && !date.isAfter(to.plusDays(PeriodEpisodes.MAX_GAP_DAYS));
    }

    private LogEntryResponse clearFlow(CycleLog entry) {
        LocalDate d = entry.getLogDate();
        entry.setFlow(null);
        boolean isNowEmpty = entry.getMood() == null && entry.getSymptoms().isEmpty()
                && (entry.getNotes() == null || entry.getNotes().isBlank()) && !entry.isIntimate();
        if (isNowEmpty) {
            cycleLogRepository.delete(entry);
            return LogEntryResponse.empty(d);
        }
        cycleLogRepository.save(entry);
        return new LogEntryResponse(d, null, entry.getMood() == null ? null : entry.getMood().name(),
                SymptomType.fromValues(entry.getSymptoms()), entry.getNotes(), entry.isIntimate());
    }

    private record ClearedPeriod(List<LogEntryResponse> entries, LocalDate from, LocalDate to) {
        boolean touches(LocalDate date) {
            return !date.isBefore(from.minusDays(PeriodEpisodes.MAX_GAP_DAYS)) && !date.isAfter(to.plusDays(PeriodEpisodes.MAX_GAP_DAYS));
        }
    }

    private ClearedPeriod clearRestOfSamePeriod(UUID userId, LocalDate newStart, LocalDate newEnd) {
        List<CycleLog> flowDays = cycleLogRepository.findByUserIdAndFlowIsNotNull(userId).stream()
                .sorted(Comparator.comparing(CycleLog::getLogDate))
                .toList();
        List<CycleLog> samePeriod = new ArrayList<>();
        LocalDate from = newStart;
        for (int i = flowDays.size() - 1; i >= 0; i--) {
            LocalDate d = flowDays.get(i).getLogDate();
            if (!d.isBefore(newStart)) {
                continue;
            }
            if (!PeriodEpisodes.sameEpisode(d, from)) {
                break;
            }
            samePeriod.add(flowDays.get(i));
            from = d;
        }
        LocalDate to = newEnd;
        for (CycleLog entry : flowDays) {
            LocalDate d = entry.getLogDate();
            if (!d.isAfter(newEnd)) {
                continue;
            }
            if (!PeriodEpisodes.sameEpisode(to, d)) {
                break;
            }
            samePeriod.add(entry);
            to = d;
        }

        List<LogEntryResponse> cleared = new ArrayList<>();
        for (CycleLog entry : samePeriod) {
            cleared.add(clearFlow(entry));
        }
        return new ClearedPeriod(cleared, from, to);
    }
}
