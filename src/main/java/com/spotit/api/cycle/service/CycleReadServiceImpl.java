package com.spotit.api.cycle.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.cycle.CycleUtil;
import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CycleReadServiceImpl implements CycleReadService {

    private final UserRepository userRepository;
    private final CycleLogRepository cycleLogRepository;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    @Transactional(readOnly = true)
    public CycleCurrentResponse getCurrent(UUID userId) {
        User user = requireUser(userId);
        // No period logged yet — there's nothing to compute a cycle day/phase/prediction from.
        // Treating "today" as an implicit period start would fabricate a confident-looking
        // result (e.g. "Day 1 · Period") out of zero real data.
        if (user.getLastPeriodDate() == null) {
            return new CycleCurrentResponse(null, null, null, null, "insufficient_data");
        }

        LocalDate today = LocalDate.now();
        LocalDate lastPeriod = user.getLastPeriodDate();

        int cycleDay = CycleUtil.cycleDayOf(today, lastPeriod, user.getCycleLength());
        CycleUtil.Phase phase = CycleUtil.phaseFor(cycleDay, user.getPeriodLength(), user.getCycleLength());
        LocalDate nextPeriod = CycleUtil.nextPeriodDate(today, lastPeriod, user.getCycleLength());
        long daysUntil = ChronoUnit.DAYS.between(today, nextPeriod);

        long totalLogs = cycleLogRepository.countByUserId(userId);
        String confidence = totalLogs >= configurationDomainService.getCycleHighConfidenceLogThreshold() ? "high" : "estimated";

        return new CycleCurrentResponse(cycleDay, phase == null ? null : phase.key().name(), nextPeriod, Math.max(0, daysUntil), confidence);
    }

    @Override
    @Transactional(readOnly = true)
    public CycleCalendarResponse getCalendarMonth(UUID userId, int year, int month) {
        if (month < 1 || month > 12) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.MONTH_OUT_OF_RANGE);
        }
        User user = requireUser(userId);
        LocalDate lastPeriod = user.getLastPeriodDate();

        LocalDate first = LocalDate.of(year, month, 1);
        int daysInMonth = first.lengthOfMonth();

        // No period logged yet — same reasoning as getCurrent(): every day gets a null phase
        // rather than one fabricated from treating today as an implicit period start.
        List<CycleCalendarResponse.DayPhase> days = first.datesUntil(first.plusDays(daysInMonth))
                .map(date -> {
                    if (lastPeriod == null) {
                        return new CycleCalendarResponse.DayPhase(date.toString(), null);
                    }
                    int cycleDay = CycleUtil.cycleDayOf(date, lastPeriod, user.getCycleLength());
                    CycleUtil.Phase phase = CycleUtil.phaseFor(cycleDay, user.getPeriodLength(), user.getCycleLength());
                    return new CycleCalendarResponse.DayPhase(date.toString(), phase == null ? null : phase.key().name());
                })
                .toList();

        return new CycleCalendarResponse(year, month, days);
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
