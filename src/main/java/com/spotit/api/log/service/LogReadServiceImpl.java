package com.spotit.api.log.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogsRangeResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.SymptomType;
import com.spotit.api.log.repository.CycleLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogReadServiceImpl implements LogReadService {
    private final CycleLogRepository cycleLogRepository;

    @Override
    @Transactional(readOnly = true)
    public LogEntryResponse getLog(UUID userId, LocalDate date) {
        return cycleLogRepository.findByUserIdAndLogDate(userId, date)
                .map(this::toEntryResponse)
                .orElseGet(() -> LogEntryResponse.empty(date));
    }

    @Override
    @Transactional(readOnly = true)
    public LogsRangeResponse getLogsInRange(UUID userId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.INVALID_DATE_RANGE);
        }
        Map<String, LogEntryResponse> logs = new LinkedHashMap<>();
        cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, from, to)
                .forEach(log -> logs.put(log.getLogDate().toString(), toEntryResponse(log)));
        return new LogsRangeResponse(logs);
    }

    private LogEntryResponse toEntryResponse(CycleLog log) {
        return new LogEntryResponse(
                log.getLogDate(),
                log.getFlow() == null ? null : log.getFlow().name(),
                log.getMood() == null ? null : log.getMood().name(),
                SymptomType.fromValues(log.getSymptoms()),
                log.getNotes(),
                log.isIntimate()
        );
    }
}
