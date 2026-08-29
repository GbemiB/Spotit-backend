package com.spotit.api.rewards.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.rewards.dto.*;
import com.spotit.api.rewards.service.BadgeReadService;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.ChallengeWriteService;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.rewards.service.RewardsReadService;
import com.spotit.api.rewards.service.RewardsWriteService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Rewards", description = "SpotPoints balance, daily claims, rewarded ads, points history, badges, and weekly challenges.")
@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardsController {
    private final RewardsReadService rewardsReadService;
    private final RewardsWriteService rewardsWriteService;
    private final BadgeReadService badgeReadService;
    private final ChallengeReadService challengeReadService;
    private final ChallengeWriteService challengeWriteService;
    private final LevelDefinitionService levelDefinitionService;

    @Operation(summary = "List level tiers", description = "SpotPoints level tiers in progression order — was a hardcoded client-side array, now admin-configurable.",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Level tiers.", content = @Content(
                    schema = @Schema(implementation = LevelDefinitionResponse.class)))
    })
    @GetMapping("/levels")
    public List<LevelDefinitionResponse> levels() {
        return levelDefinitionService.getLevels();
    }

    @Operation(summary = RewardsControllerSwagger.SUMMARY_SUMMARY, description = RewardsControllerSwagger.SUMMARY_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rewards summary.", content = @Content(
                    schema = @Schema(implementation = RewardsSummaryResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.SUMMARY_200_EXAMPLE)))
    })
    @GetMapping("/summary")
    public RewardsSummaryResponse summary(@CurrentUserId UUID userId) {
        return rewardsReadService.getSummary(userId);
    }

    @Operation(summary = RewardsControllerSwagger.DAILY_CLAIM_SUMMARY, description = RewardsControllerSwagger.DAILY_CLAIM_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daily claim processed.", content = @Content(
                    schema = @Schema(implementation = DailyClaimResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.DAILY_CLAIM_200_EXAMPLE)))
    })
    @PostMapping("/daily-claim")
    public DailyClaimResponse dailyClaim(@CurrentUserId UUID userId) {
        return rewardsWriteService.claimDaily(userId);
    }

    @Operation(summary = RewardsControllerSwagger.WATCH_AD_SUMMARY, description = RewardsControllerSwagger.WATCH_AD_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = AdWatchRequest.class),
            examples = @ExampleObject(value = RewardsControllerSwagger.WATCH_AD_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ad-watch points awarded.", content = @Content(
                    schema = @Schema(implementation = AdWatchResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.WATCH_AD_200_EXAMPLE))),
            @ApiResponse(responseCode = "429", description = "You've reached today's rewarded-ad limit.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.WATCH_AD_429_EXAMPLE)))
    })
    @PostMapping("/watch-ad")
    public AdWatchResponse watchAd(@CurrentUserId UUID userId, @Valid @RequestBody AdWatchRequest request) {
        return rewardsWriteService.watchAd(userId);
    }

    @Operation(summary = RewardsControllerSwagger.HISTORY_SUMMARY, description = RewardsControllerSwagger.HISTORY_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Points history page.", content = @Content(
                    schema = @Schema(implementation = PointsHistoryPageResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.HISTORY_200_EXAMPLE)))
    })
    @GetMapping("/history")
    public PointsHistoryPageResponse history(@CurrentUserId UUID userId,
                                              @Parameter(description = "Page size; defaults to 20.", example = "20") @RequestParam(required = false) Integer limit,
                                              @Parameter(description = "Opaque pagination cursor from a previous page's nextCursor.", example = "MjA=") @RequestParam(required = false) String cursor) {
        return rewardsReadService.getHistory(userId, limit, cursor);
    }

    @Operation(summary = RewardsControllerSwagger.BADGES_SUMMARY, description = RewardsControllerSwagger.BADGES_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Badges with earned status.", content = @Content(
                    schema = @Schema(implementation = BadgeResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.BADGES_200_EXAMPLE)))
    })
    @GetMapping("/badges")
    public List<BadgeResponse> badges(@CurrentUserId UUID userId) {
        return badgeReadService.getBadgesSyncingNewlyEarned(userId);
    }

    @Operation(summary = RewardsControllerSwagger.CHALLENGES_SUMMARY, description = RewardsControllerSwagger.CHALLENGES_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weekly challenges with progress.", content = @Content(
                    schema = @Schema(implementation = ChallengeResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.CHALLENGES_200_EXAMPLE)))
    })
    @GetMapping("/challenges")
    public List<ChallengeResponse> challenges(@CurrentUserId UUID userId) {
        return challengeReadService.getChallenges(userId);
    }

    @Operation(summary = RewardsControllerSwagger.CLAIM_CHALLENGE_SUMMARY, description = RewardsControllerSwagger.CLAIM_CHALLENGE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reward claimed.", content = @Content(
                    schema = @Schema(implementation = ChallengeClaimResponse.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.CLAIM_CHALLENGE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Challenge not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.CLAIM_CHALLENGE_404_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "This challenge isn't complete yet, or its reward was already claimed this week.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = RewardsControllerSwagger.CLAIM_CHALLENGE_409_EXAMPLE)))
    })
    @PostMapping("/challenges/{id}/claim")
    public ChallengeClaimResponse claimChallenge(@CurrentUserId UUID userId, @Parameter(description = "Challenge definition id", example = "streak_7") @PathVariable("id") String challengeId) {
        return challengeWriteService.claim(userId, challengeId);
    }
}
