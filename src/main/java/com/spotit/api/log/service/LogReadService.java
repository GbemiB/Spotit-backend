package com.spotit.api.log.service;

import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogsRangeResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface LogReadService {
    LogEntryResponse getLog(UUID userId, LocalDate date);

    LogsRangeResponse getLogsInRange(UUID userId, LocalDate from, LocalDate to);
}
