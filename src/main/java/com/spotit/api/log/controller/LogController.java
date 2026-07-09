package com.spotit.api.log.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogsRangeResponse;
import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;
import com.spotit.api.log.service.LogReadService;
import com.spotit.api.log.service.LogWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogReadService logReadService;
    private final LogWriteService logWriteService;

    @PutMapping("/{date}")
    public SaveLogResponse saveLog(@CurrentUserId UUID userId,
                                    @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    @Valid @RequestBody SaveLogRequest request) {
        return logWriteService.saveLog(userId, date, request);
    }

    @GetMapping("/{date}")
    public LogEntryResponse getLog(@CurrentUserId UUID userId,
                                    @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return logReadService.getLog(userId, date);
    }

    @GetMapping
    public LogsRangeResponse getLogsInRange(@CurrentUserId UUID userId,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return logReadService.getLogsInRange(userId, from, to);
    }

    @DeleteMapping("/{date}")
    public MessageResponse deleteLog(@CurrentUserId UUID userId,
                                      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logWriteService.deleteLog(userId, date);
        return new MessageResponse("Entry deleted.");
    }
}
