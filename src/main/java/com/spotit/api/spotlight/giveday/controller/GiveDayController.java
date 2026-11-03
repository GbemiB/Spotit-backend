package com.spotit.api.spotlight.giveday.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.spotlight.giveday.dto.GiveDayAreaResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayFinishResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayGiveResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayHomeRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayHomeResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayStartResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelRequest;
import com.spotit.api.spotlight.giveday.dto.GiveDayTravelResponse;
import com.spotit.api.spotlight.giveday.dto.GiveDayTripsResponse;
import com.spotit.api.spotlight.giveday.service.GiveDayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Give Day", description = "The Spotlight game where players travel across Lagos and give period gifts to places, within a clock and SP gift budgets. "
        + "Pretend only: no real products or money. Kits are scored on the server.")
@RestController
@RequestMapping("/api/v1/giveday")
@RequiredArgsConstructor
public class GiveDayController {
    private final GiveDayService giveDayService;

    @Operation(summary = "Start a day", description = "Charges the challenge's SP cost (0 = free) and returns the clock, the player's home area "
            + "(null until picked), Give Day stars and level, levels, ways to travel, areas of Lagos with their level, and products.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs")
    public GiveDayStartResponse start(@CurrentUserId UUID userId) {
        return giveDayService.start(userId);
    }

    @Operation(summary = "Set the player's home area", description = "Levels are rings around home. A day that hasn't travelled yet "
            + "moves to the new home. Returns every area with its level from the new home.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/home")
    public GiveDayHomeResponse home(@CurrentUserId UUID userId, @Valid @RequestBody GiveDayHomeRequest request) {
        return giveDayService.home(userId, request);
    }

    @Operation(summary = "Places in an area", description = "The area's level from home, whether it is open to the player, any day pass "
            + "needed, the gift budget, and its places with closing times and today's stars.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/runs/{runId}/areas/{areaId}")
    public GiveDayAreaResponse area(@CurrentUserId UUID userId, @PathVariable UUID runId, @PathVariable String areaId) {
        return giveDayService.area(userId, runId, areaId);
    }

    @Operation(summary = "Ways to reach a place", description = "For each way to travel: SP cost, minutes, arrival time and whether the place "
            + "is still open on arrival. Also the place's level, whether it is open to the player, any day pass needed and the gift budget.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/runs/{runId}/trips/{placeId}")
    public GiveDayTripsResponse trips(@CurrentUserId UUID userId, @PathVariable UUID runId, @PathVariable String placeId) {
        return giveDayService.trips(userId, runId, placeId);
    }

    @Operation(summary = "Travel to a place", description = "Charges the way's SP and any level day pass, moves the clock to the arrival time "
            + "and the player to the place. Refused if the level is locked, the place closes before the visit ends or SP is short.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/travel")
    public GiveDayTravelResponse travel(@CurrentUserId UUID userId, @PathVariable UUID runId, @Valid @RequestBody GiveDayTravelRequest request) {
        return giveDayService.travel(userId, runId, request);
    }

    @Operation(summary = "Give a kit at a place", description = "The player must have travelled here. The gift covers everyone at the place and must "
            + "fit the level's gift budget. Returns 0 to 3 stars, a title, each product's rating and why, and the best products missed.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/give")
    public GiveDayGiveResponse give(@CurrentUserId UUID userId, @PathVariable UUID runId, @Valid @RequestBody GiveDayGiveRequest request) {
        return giveDayService.give(userId, runId, request);
    }

    @Operation(summary = "End the day", description = "Closes the day with stars (the average of the places helped), people helped and SP spent.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/finish")
    public GiveDayFinishResponse finish(@CurrentUserId UUID userId, @PathVariable UUID runId) {
        return giveDayService.finish(userId, runId);
    }
}
