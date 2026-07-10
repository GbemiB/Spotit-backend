package com.spotit.api.insight.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.insight.dto.CycleTrendsResponse;
import com.spotit.api.insight.dto.RegularityResponse;
import com.spotit.api.insight.dto.WeeklyDigestResponse;
import com.spotit.api.insight.service.InsightReadService;
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

@Tag(name = "Insights", description = "Derived cycle analytics: trends, weekly digest, and regularity flags.")
@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class InsightController {

    private final InsightReadService insightReadService;

    @Operation(summary = InsightControllerSwagger.TRENDS_SUMMARY, description = InsightControllerSwagger.TRENDS_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cycle trends.", content = @Content(
                    schema = @Schema(implementation = CycleTrendsResponse.class),
                    examples = @ExampleObject(value = InsightControllerSwagger.TRENDS_200_EXAMPLE)))
    })
    @GetMapping("/trends")
    public CycleTrendsResponse trends(@CurrentUserId UUID userId, @Parameter(description = "Number of recent cycles to include; defaults to 6.", example = "6") @RequestParam(required = false) Integer cycles) {
        return insightReadService.getTrends(userId, cycles);
    }

    @Operation(summary = InsightControllerSwagger.WEEKLY_DIGEST_SUMMARY, description = InsightControllerSwagger.WEEKLY_DIGEST_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weekly digest.", content = @Content(
                    schema = @Schema(implementation = WeeklyDigestResponse.class),
                    examples = @ExampleObject(value = InsightControllerSwagger.WEEKLY_DIGEST_200_EXAMPLE)))
    })
    @GetMapping("/digest/weekly")
    public WeeklyDigestResponse weeklyDigest(@CurrentUserId UUID userId) {
        return insightReadService.getWeeklyDigest(userId);
    }

    @Operation(summary = InsightControllerSwagger.REGULARITY_SUMMARY, description = InsightControllerSwagger.REGULARITY_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Regularity assessment.", content = @Content(
                    schema = @Schema(implementation = RegularityResponse.class),
                    examples = @ExampleObject(value = InsightControllerSwagger.REGULARITY_200_EXAMPLE)))
    })
    @GetMapping("/regularity")
    public RegularityResponse regularity(@CurrentUserId UUID userId) {
        return insightReadService.getRegularity(userId);
    }
}
