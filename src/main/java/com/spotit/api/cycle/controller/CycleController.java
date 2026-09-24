package com.spotit.api.cycle.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.cycle.service.CycleReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Cycle", description = "Cycle-phase computation: current day/phase and calendar month view.")
@RestController
@RequestMapping("/api/v1/cycle")
@RequiredArgsConstructor
public class CycleController {
    private final CycleReadService cycleReadService;

    @Operation(summary = CycleControllerSwagger.CURRENT_SUMMARY, description = CycleControllerSwagger.CURRENT_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current cycle status.", content = @Content(
                    schema = @Schema(implementation = CycleCurrentResponse.class),
                    examples = @ExampleObject(value = CycleControllerSwagger.CURRENT_200_EXAMPLE)))
    })
    @GetMapping("/current")
    public CycleCurrentResponse current(@CurrentUserId UUID userId) {
        return cycleReadService.getCurrent(userId);
    }

    @Operation(summary = CycleControllerSwagger.CALENDAR_SUMMARY, description = CycleControllerSwagger.CALENDAR_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Calendar for the requested month.", content = @Content(
                    schema = @Schema(implementation = CycleCalendarResponse.class),
                    examples = @ExampleObject(value = CycleControllerSwagger.CALENDAR_200_EXAMPLE))),
            @ApiResponse(responseCode = "422", description = "month must be between 1 and 12.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = CycleControllerSwagger.CALENDAR_422_EXAMPLE)))
    })
    @GetMapping("/calendar")
    public CycleCalendarResponse calendar(@CurrentUserId UUID userId,
                                           @Parameter(description = "Calendar year", example = "2026") @RequestParam int year,
                                           @Parameter(description = "Calendar month (1-12)", example = "7") @RequestParam int month) {
        return cycleReadService.getCalendarMonth(userId, year, month);
    }
}
