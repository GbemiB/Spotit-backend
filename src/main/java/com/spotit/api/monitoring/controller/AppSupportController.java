package com.spotit.api.monitoring.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.monitoring.dto.CrashReportRequest;
import com.spotit.api.monitoring.dto.SupportResponse;
import com.spotit.api.monitoring.service.ActivityFilter;
import com.spotit.api.monitoring.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "App support", description = "Crash reports from the app and the customer support contact.")
@RestController
@RequestMapping("/api/v1/app")
@RequiredArgsConstructor
public class AppSupportController {
    private final MonitoringService monitoringService;

    @Operation(summary = "Report an app crash", description = "Public, so crashes before sign-in are caught too; the user is attached "
            + "when a valid token is sent. Long fields are cut to fit. Admins read them through /api/v1/config/reports.")
    @PostMapping("/crashes")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse reportCrash(@Valid @RequestBody CrashReportRequest request) {
        monitoringService.recordCrash(ActivityFilter.currentUserId(), request);
        return new MessageResponse("Crash recorded.");
    }

    @Operation(summary = "Get the support contact", description = "Public. The email the app’s Contact support row opens, set by the "
            + "app-support-email property in the admin config.")
    @GetMapping("/support")
    public SupportResponse support() {
        return new SupportResponse(monitoringService.supportEmail());
    }
}
