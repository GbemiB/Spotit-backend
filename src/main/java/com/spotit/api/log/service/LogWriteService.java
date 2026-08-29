package com.spotit.api.log.service;

import com.spotit.api.log.dto.LogPeriodRequest;
import com.spotit.api.log.dto.LogPeriodResponse;
import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;

import java.time.LocalDate;
import java.util.UUID;

public interface LogWriteService {
    SaveLogResponse saveLog(UUID userId, LocalDate date, SaveLogRequest request);

    LogPeriodResponse logPeriod(UUID userId, LogPeriodRequest request);

    void deleteLog(UUID userId, LocalDate date);
}
