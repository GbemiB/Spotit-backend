package com.spotit.api.monitoring.controller;

import com.spotit.api.monitoring.dto.CrashPage;
import com.spotit.api.monitoring.dto.ReportSummaryResponse;
import com.spotit.api.monitoring.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "Reports (Admin)", description = "Monitoring report: downloads, sign-ups, active users, crashes, failed actions, last activity.")
@RestController
@RequestMapping("/api/v1/config/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "Monitoring summary", description = "Dates are UTC days, inclusive, up to 366 days. downloads counts users who "
            + "finished their first onboarding in the range (owners and partners, by the platform of their device). active counts users "
            + "seen in the day, 7 days and 30 days up to the end of the range. crashes and failedActions cover the range; lastActivity is "
            + "every user, most recently active first, paged by page and size (max 200).")
    @GetMapping("/summary")
    public ReportSummaryResponse summary(
            @Parameter(example = "2026-10-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(example = "2026-10-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return reportService.summary(from, to, page, size);
    }

    @Operation(summary = "Crash list", description = "Every crash in the range, newest first, with stack, screen, app version and phone.")
    @GetMapping("/crashes")
    public CrashPage crashes(
            @Parameter(example = "2026-10-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(example = "2026-10-31") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return reportService.crashes(from, to, page, size);
    }
}
