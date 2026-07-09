package com.spotit.api.rewards.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.rewards.dto.*;
import com.spotit.api.rewards.service.BadgeReadService;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.ChallengeWriteService;
import com.spotit.api.rewards.service.RewardsReadService;
import com.spotit.api.rewards.service.RewardsWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rewards")
@RequiredArgsConstructor
public class RewardsController {

    private final RewardsReadService rewardsReadService;
    private final RewardsWriteService rewardsWriteService;
    private final BadgeReadService badgeReadService;
    private final ChallengeReadService challengeReadService;
    private final ChallengeWriteService challengeWriteService;

    @GetMapping("/summary")
    public RewardsSummaryResponse summary(@CurrentUserId UUID userId) {
        return rewardsReadService.getSummary(userId);
    }

    @PostMapping("/daily-claim")
    public DailyClaimResponse dailyClaim(@CurrentUserId UUID userId) {
        return rewardsWriteService.claimDaily(userId);
    }

    @PostMapping("/watch-ad")
    public AdWatchResponse watchAd(@CurrentUserId UUID userId, @Valid @RequestBody AdWatchRequest request) {
        return rewardsWriteService.watchAd(userId);
    }

    @GetMapping("/history")
    public PointsHistoryPageResponse history(@CurrentUserId UUID userId,
                                              @RequestParam(required = false) Integer limit,
                                              @RequestParam(required = false) String cursor) {
        return rewardsReadService.getHistory(userId, limit, cursor);
    }

    @GetMapping("/badges")
    public List<BadgeResponse> badges(@CurrentUserId UUID userId) {
        return badgeReadService.getBadgesSyncingNewlyEarned(userId);
    }

    @GetMapping("/challenges")
    public List<ChallengeResponse> challenges(@CurrentUserId UUID userId) {
        return challengeReadService.getChallenges(userId);
    }

    @PostMapping("/challenges/{id}/claim")
    public ChallengeClaimResponse claimChallenge(@CurrentUserId UUID userId, @PathVariable("id") String challengeId) {
        return challengeWriteService.claim(userId, challengeId);
    }
}
