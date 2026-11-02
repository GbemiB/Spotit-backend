package com.spotit.api.spotlight.giveday.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.service.GiveDayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Give Day", description = "The Spotlight game where players spend pretend give coins on period kits for places in town. "
        + "Pretend only: no real products or money. Kits are scored on the server.")
@RestController
@RequestMapping("/api/v1/giveday")
@RequiredArgsConstructor
public class GiveDayController {
    private final GiveDayService giveDayService;

    @Operation(summary = "Start a day", description = "Charges the challenge's SP cost (or uses a free try) and returns the give coins, the places "
            + "on the map with their hosts, needs facts and group sizes, and the products on the shelf.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs")
    public GiveDayStartResponse start(@CurrentUserId UUID userId) {
        return giveDayService.start(userId);
    }

    @Operation(summary = "Give a kit at a place", description = "people must be one of the place's group sizes; the kit costs the products' coins "
            + "times people. Returns 0 to 3 stars, a title, each product's rating and why, and the best products missed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/give")
    public GiveDayGiveResponse give(@CurrentUserId UUID userId, @PathVariable UUID runId, @Valid @RequestBody GiveDayGiveRequest request) {
        return giveDayService.give(userId, runId, request);
    }

    @Operation(summary = "End the day", description = "Closes the day with stars (the average of the places helped), people helped and coins left.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/finish")
    public GiveDayFinishResponse finish(@CurrentUserId UUID userId, @PathVariable UUID runId) {
        return giveDayService.finish(userId, runId);
    }
}
