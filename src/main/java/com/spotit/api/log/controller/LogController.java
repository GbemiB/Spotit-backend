package com.spotit.api.log.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.i18n.EnumMessages;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.log.dto.EnumOption;
import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogPeriodRequest;
import com.spotit.api.log.dto.LogPeriodResponse;
import com.spotit.api.log.dto.LogTemplateResponse;
import com.spotit.api.log.dto.LogsRangeResponse;
import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.entity.SymptomType;
import com.spotit.api.log.service.LogReadService;
import com.spotit.api.log.service.LogWriteService;
import com.spotit.api.rewards.service.ChallengeReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@Tag(name = "Logs", description = "Daily cycle log entries: flow, mood, symptoms, and notes.")
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogReadService logReadService;
    private final LogWriteService logWriteService;
    private final ChallengeReadService challengeReadService;

    @Operation(summary = LogControllerSwagger.TEMPLATE_SUMMARY, description = LogControllerSwagger.TEMPLATE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log template.", content = @Content(
                    schema = @Schema(implementation = LogTemplateResponse.class),
                    examples = @ExampleObject(value = LogControllerSwagger.TEMPLATE_200_EXAMPLE)))
    })
    @GetMapping("/template")
    public LogTemplateResponse template() {
        return new LogTemplateResponse(
                Arrays.stream(FlowIntensity.values()).map(f -> new EnumOption(f.name(), EnumMessages.resolve(f.getCode()))).toList(),
                Arrays.stream(MoodType.values()).map(m -> new EnumOption(m.name(), EnumMessages.resolve(m.getCode()))).toList(),
                Arrays.stream(SymptomType.values()).map(s -> new EnumOption(s.name(), EnumMessages.resolve(s.getCode()))).toList(),
                challengeReadService.getDailyLogReward()
        );
    }

    @Operation(summary = LogControllerSwagger.SAVE_LOG_SUMMARY, description = LogControllerSwagger.SAVE_LOG_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = SaveLogRequest.class),
            examples = @ExampleObject(value = LogControllerSwagger.SAVE_LOG_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log saved.", content = @Content(
                    schema = @Schema(implementation = SaveLogResponse.class),
                    examples = @ExampleObject(value = LogControllerSwagger.SAVE_LOG_200_EXAMPLE)))
    })
    @PutMapping("/{date}")
    public SaveLogResponse saveLog(@CurrentUserId UUID userId,
                                    @Parameter(description = "Log date (ISO-8601)", example = "2026-07-10") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    @Valid @RequestBody SaveLogRequest request) {
        return logWriteService.saveLog(userId, date, request);
    }

    @Operation(summary = LogControllerSwagger.LOG_PERIOD_SUMMARY, description = LogControllerSwagger.LOG_PERIOD_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = LogPeriodRequest.class),
            examples = @ExampleObject(value = LogControllerSwagger.LOG_PERIOD_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Period logged.", content = @Content(
                    schema = @Schema(implementation = LogPeriodResponse.class),
                    examples = @ExampleObject(value = LogControllerSwagger.LOG_PERIOD_200_EXAMPLE))),
            @ApiResponse(responseCode = "422", description = "Invalid date range.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = LogControllerSwagger.LOG_PERIOD_422_EXAMPLE)))
    })
    @PutMapping("/period")
    public LogPeriodResponse logPeriod(@CurrentUserId UUID userId, @Valid @RequestBody LogPeriodRequest request) {
        return logWriteService.logPeriod(userId, request);
    }

    @Operation(summary = LogControllerSwagger.GET_LOG_SUMMARY, description = LogControllerSwagger.GET_LOG_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log entry (or empty entry if none logged).", content = @Content(
                    schema = @Schema(implementation = LogEntryResponse.class),
                    examples = @ExampleObject(value = LogControllerSwagger.GET_LOG_200_EXAMPLE)))
    })
    @GetMapping("/{date}")
    public LogEntryResponse getLog(@CurrentUserId UUID userId,
                                    @Parameter(description = "Log date (ISO-8601)", example = "2026-07-10") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return logReadService.getLog(userId, date);
    }

    @Operation(summary = LogControllerSwagger.GET_LOGS_RANGE_SUMMARY, description = LogControllerSwagger.GET_LOGS_RANGE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logs in range.", content = @Content(
                    schema = @Schema(implementation = LogsRangeResponse.class),
                    examples = @ExampleObject(value = LogControllerSwagger.GET_LOGS_RANGE_200_EXAMPLE))),
            @ApiResponse(responseCode = "422", description = "'from' must be before or equal to 'to'.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = LogControllerSwagger.GET_LOGS_RANGE_422_EXAMPLE)))
    })
    @GetMapping
    public LogsRangeResponse getLogsInRange(@CurrentUserId UUID userId,
                                             @Parameter(description = "Range start date (inclusive)", example = "2026-07-01") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                             @Parameter(description = "Range end date (inclusive)", example = "2026-07-10") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return logReadService.getLogsInRange(userId, from, to);
    }

    @Operation(summary = LogControllerSwagger.DELETE_LOG_SUMMARY, description = LogControllerSwagger.DELETE_LOG_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry deleted.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = LogControllerSwagger.DELETE_LOG_200_EXAMPLE)))
    })
    @DeleteMapping("/{date}")
    public MessageResponse deleteLog(@CurrentUserId UUID userId,
                                      @Parameter(description = "Log date (ISO-8601)", example = "2026-07-10") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logWriteService.deleteLog(userId, date);
        return new MessageResponse("Entry deleted.");
    }
}
