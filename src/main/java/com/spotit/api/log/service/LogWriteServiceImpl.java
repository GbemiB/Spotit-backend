package com.spotit.api.log.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogPeriodRequest;
import com.spotit.api.log.dto.LogPeriodResponse;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogWriteServiceImpl implements LogWriteService {

    private static final int MAX_PERIOD_RANGE_DAYS = 14;

    private final CycleLogRepository cycleLogRepository;
    private final PointsWriteService pointsWriteService;
    private final UserRepository userRepository;

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
        if (ChronoUnit.DAYS.between(startDate, endDate) + 1 > MAX_PERIOD_RANGE_DAYS) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PERIOD_RANGE_TOO_LONG);
        }

        LocalDate detailDate = request.detailDate() != null ? request.detailDate() : startDate;
        if (detailDate.isBefore(startDate) || detailDate.isAfter(endDate)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.PERIOD_DETAIL_DATE_OUT_OF_RANGE);
        }

        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));

        LogEntryResponse startDayEntry = null;
        long pointsAwarded = 0;
        long newBalance = user.getPoints();
        int streak = user.getStreak();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            var existing = cycleLogRepository.findByUserIdAndLogDate(userId, currentDate);
            boolean isNewEntry = existing.isEmpty();
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

            // Points/streak are awarded once per logPeriod action (on the detail day), not once
            // per day in the range — otherwise a multi-day backfill earns N x the daily reward
            // and can inflate the streak counter multiple times within a single request.
            if (isDetailDay) {
                var pointsResult = pointsWriteService.recordDailyLog(userId, currentDate, isNewEntry);
                pointsAwarded = pointsResult.pointsAwarded();
                newBalance = pointsResult.newBalance();
                streak = pointsResult.streak();
                startDayEntry = new LogEntryResponse(
                        currentDate, request.flow(), request.mood(), SymptomType.fromValues(entry.getSymptoms()), entry.getNotes(), entry.isIntimate());
            }
        }

        List<LogEntryResponse> clearedEntries = clearStalePeriodDays(userId, startDate, endDate);

        LocalDate lastPeriod = user.getLastPeriodDate();
        boolean shouldResync = lastPeriod == null
                || !startDate.isBefore(lastPeriod)
                || ChronoUnit.DAYS.between(startDate, lastPeriod) <= user.getCycleLength() / 2;
        if (shouldResync) {
            user.setLastPeriodDate(startDate);
            userRepository.save(user);
        }

        return new LogPeriodResponse(
                startDate, endDate, request.flow(), user.getLastPeriodDate(), user.getCycleLength(), user.getPeriodLength(),
                startDayEntry, pointsAwarded, newBalance, streak, clearedEntries
        );
    }

    @Override
    @Transactional
    public void deleteLog(UUID userId, LocalDate date) {
        cycleLogRepository.deleteByUserIdAndLogDate(userId, date);
    }

    // Only the most recently logged period should ever show as flow-logged — logging a new one
    // clears the flow on every previously flow-logged day outside this range (deleting the row
    // outright if it had no other data, or just nulling flow if it did), not just days adjacent
    // to this range. Returns each cleared day's corrected state so the caller can fix its own
    // cached copy — clearing the row here alone doesn't help a client that already has the day
    // cached from before this edit.
    private List<LogEntryResponse> clearStalePeriodDays(UUID userId, LocalDate newStart, LocalDate newEnd) {
        List<LogEntryResponse> cleared = new ArrayList<>();
        for (CycleLog entry : cycleLogRepository.findByUserIdAndFlowIsNotNull(userId)) {
            LocalDate d = entry.getLogDate();
            if (!d.isBefore(newStart) && !d.isAfter(newEnd)) {
                continue;
            }
            entry.setFlow(null);
            boolean isNowEmpty = entry.getMood() == null && entry.getSymptoms().isEmpty()
                    && (entry.getNotes() == null || entry.getNotes().isBlank()) && !entry.isIntimate();
            if (isNowEmpty) {
                cycleLogRepository.delete(entry);
                cleared.add(LogEntryResponse.empty(d));
            } else {
                cycleLogRepository.save(entry);
                cleared.add(new LogEntryResponse(d, null, entry.getMood() == null ? null : entry.getMood().name(),
                        SymptomType.fromValues(entry.getSymptoms()), entry.getNotes(), entry.isIntimate()));
            }
        }
        return cleared;
    }
}
