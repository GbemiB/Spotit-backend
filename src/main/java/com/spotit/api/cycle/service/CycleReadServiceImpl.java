package com.spotit.api.cycle.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.cycle.CyclePhase;
import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CycleReadServiceImpl implements CycleReadService {
    private static final String EXPECTED = "expected";
    private static final String MISSED = "missed";

    private final UserRepository userRepository;
    private final CycleLogRepository cycleLogRepository;
    private final ConfigurationDomainService configurationDomainService;
    private final UserClock userClock;

    @Override
    @Transactional(readOnly = true)
    public CycleCurrentResponse getCurrent(UUID userId) {
        return getCurrent(userId, userClock.today(requireUser(userId)));
    }

    CycleCurrentResponse getCurrent(UUID userId, LocalDate today) {
        User user = requireUser(userId);

        if (user.getLastPeriodDate() == null) {
            return CycleCurrentResponse.insufficientData();
        }

        LocalDate lastPeriod = user.getLastPeriodDate();
        CycleUtil.Status status = CycleUtil.statusOf(today, lastPeriod, user.getCycleLength());

        String phaseKey = null;
        LocalDate nextPeriod = status.expectedDate();
        if (status.tracking()) {
            CycleUtil.Phase phase = CycleUtil.phaseFor(status.cycleDay(), user.getPeriodLength(), user.getCycleLength());
            phaseKey = phase == null ? null : phase.key().name();
            nextPeriod = CycleUtil.nextPeriodDate(today, lastPeriod, user.getCycleLength());
        }
        long daysUntil = Math.max(0, ChronoUnit.DAYS.between(today, nextPeriod));

        long totalLogs = cycleLogRepository.countByUserId(userId);
        String confidence = totalLogs >= configurationDomainService.getCycleHighConfidenceLogThreshold() ? "high" : "estimated";

        return new CycleCurrentResponse(status.cycleDay(), phaseKey, nextPeriod, daysUntil, confidence, status.state(), status.daysLate(),
                status.expectedDate(), status.missedCycles(), CycleUtil.trackingStartOf(user.getTrackingStartDate(), lastPeriod));
    }

    @Override
    @Transactional(readOnly = true)
    public CycleCalendarResponse getCalendarMonth(UUID userId, int year, int month) {
        if (month < 1 || month > 12) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.MONTH_OUT_OF_RANGE);
        }
        return getCalendarMonth(userId, year, month, userClock.today(requireUser(userId)));
    }

    CycleCalendarResponse getCalendarMonth(UUID userId, int year, int month, LocalDate today) {
        if (month < 1 || month > 12) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.MONTH_OUT_OF_RANGE);
        }
        User user = requireUser(userId);
        LocalDate lastPeriod = user.getLastPeriodDate();
        LocalDate trackingStart = CycleUtil.trackingStartOf(user.getTrackingStartDate(), lastPeriod);
        Set<LocalDate> periodDays = cycleLogRepository.findByUserIdAndFlowIsNotNull(userId).stream()
                .map(CycleLog::getLogDate)
                .collect(Collectors.toSet());
        boolean loggedThisCycle = lastPeriod != null && periodDays.stream()
                .anyMatch(d -> !d.isBefore(lastPeriod) && d.isBefore(lastPeriod.plusDays(user.getCycleLength())));

        LocalDate first = LocalDate.of(year, month, 1);
        List<CycleCalendarResponse.DayPhase> days = first.datesUntil(first.plusMonths(1))
                .map(date -> dayOf(date, user, lastPeriod, trackingStart, periodDays, loggedThisCycle, today))
                .toList();

        return new CycleCalendarResponse(year, month, days);
    }

    private static CycleCalendarResponse.DayPhase dayOf(LocalDate date, User user, LocalDate lastPeriod, LocalDate trackingStart,
                                                        Set<LocalDate> periodDays, boolean loggedThisCycle, LocalDate today) {
        String iso = date.toString();
        if (trackingStart != null && date.isBefore(trackingStart)) {
            return new CycleCalendarResponse.DayPhase(iso, null, false, null);
        }
        if (periodDays.contains(date)) {
            return new CycleCalendarResponse.DayPhase(iso, CyclePhase.period.name(), false, null);
        }
        if (lastPeriod == null || date.isBefore(lastPeriod)) {
            return new CycleCalendarResponse.DayPhase(iso, null, false, null);
        }
        int cycleLength = user.getCycleLength();
        long diff = ChronoUnit.DAYS.between(lastPeriod, date);
        if (diff < cycleLength) {
            CycleUtil.Phase phase = CycleUtil.phaseFor((int) diff + 1, user.getPeriodLength(), cycleLength);
            boolean hidden = phase == null || (loggedThisCycle && phase.key() == CyclePhase.period);
            return new CycleCalendarResponse.DayPhase(iso, hidden ? null : phase.key().name(), false, null);
        }
        boolean expectedStart = diff % cycleLength == 0;
        if (date.isBefore(today)) {
            String marker = expectedStart ? (date.plusDays(cycleLength).isAfter(today) ? EXPECTED : MISSED) : null;
            return new CycleCalendarResponse.DayPhase(iso, null, false, marker);
        }
        CycleUtil.Phase phase = CycleUtil.phaseFor(CycleUtil.cycleDayOf(date, lastPeriod, cycleLength), user.getPeriodLength(), cycleLength);
        String marker = expectedStart && date.equals(today) ? EXPECTED : null;
        return new CycleCalendarResponse.DayPhase(iso, phase == null ? null : phase.key().name(), true, marker);
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
