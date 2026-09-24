package com.spotit.api.rewards.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.AdWatchRequest;
import com.spotit.api.rewards.dto.AdWatchResponse;
import com.spotit.api.rewards.dto.BadgeResponse;
import com.spotit.api.rewards.dto.ChallengeClaimResponse;
import com.spotit.api.rewards.dto.ChallengeResponse;
import com.spotit.api.rewards.dto.DailyClaimResponse;
import com.spotit.api.rewards.dto.LevelDefinitionResponse;
import com.spotit.api.rewards.dto.PointsHistoryEntryResponse;
import com.spotit.api.rewards.dto.PointsHistoryPageResponse;
import com.spotit.api.rewards.dto.RewardsSummaryResponse;
import com.spotit.api.rewards.service.BadgeReadService;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.ChallengeWriteService;
import com.spotit.api.rewards.service.LevelDefinitionService;
import com.spotit.api.rewards.service.RewardsReadService;
import com.spotit.api.rewards.service.RewardsWriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RewardsControllerTest {
    @Mock RewardsReadService rewardsReadService;
    @Mock RewardsWriteService rewardsWriteService;
    @Mock BadgeReadService badgeReadService;
    @Mock ChallengeReadService challengeReadService;
    @Mock ChallengeWriteService challengeWriteService;
    @Mock LevelDefinitionService levelDefinitionService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new RewardsController(rewardsReadService, rewardsWriteService, badgeReadService,
                challengeReadService, challengeWriteService, levelDefinitionService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void levelsListsTheLevelLadder() throws Exception {
        when(levelDefinitionService.getLevels()).thenReturn(List.of(new LevelDefinitionResponse("Blush", 0, 500)));

        mvc.perform(get("/api/v1/rewards/levels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Blush"))
                .andExpect(jsonPath("$.data[0].pointsHigh").value(500));
    }

    @Test
    void summaryReturnsPointsAndLevel() throws Exception {
        when(rewardsReadService.getSummary(USER_ID)).thenReturn(new RewardsSummaryResponse(
                620, "Petal", new RewardsSummaryResponse.LevelRange(500, 2000), "Rosé", 1380L, 4, 9));

        mvc.perform(get("/api/v1/rewards/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.level").value("Petal"))
                .andExpect(jsonPath("$.data.levelRange.hi").value(2000))
                .andExpect(jsonPath("$.data.pointsToNextLevel").value(1380));
    }

    @Test
    void dailyClaimAwardsTheBonus() throws Exception {
        when(rewardsWriteService.claimDaily(USER_ID)).thenReturn(new DailyClaimResponse(5, 625, false));

        mvc.perform(post("/api/v1/rewards/daily-claim"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pointsAwarded").value(5))
                .andExpect(jsonPath("$.data.alreadyClaimedToday").value(false));
    }

    @Test
    void watchAdAwardsPoints() throws Exception {
        when(rewardsWriteService.watchAd(USER_ID)).thenReturn(new AdWatchResponse(3, 628));

        mvc.perform(post("/api/v1/rewards/watch-ad").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new AdWatchRequest("admob", "unit-1", "token"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.newBalance").value(628));
    }

    @Test
    void watchAdRequiresTheAdNetwork() throws Exception {
        mvc.perform(post("/api/v1/rewards/watch-ad").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new AdWatchRequest("", null, null))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(rewardsWriteService);
    }

    @Test
    void watchAdOverTheDailyLimitIs429() throws Exception {
        when(rewardsWriteService.watchAd(USER_ID)).thenThrow(new ApiException(ErrorCode.DAILY_AD_LIMIT_REACHED, "Limit reached"));

        mvc.perform(post("/api/v1/rewards/watch-ad").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new AdWatchRequest("admob", null, null))))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.data.errorCode").value("daily_ad_limit_reached"));
    }

    @Test
    void historyPassesPagingParameters() throws Exception {
        when(rewardsReadService.getHistory(USER_ID, 20, "MjA=")).thenReturn(new PointsHistoryPageResponse(
                List.of(new PointsHistoryEntryResponse("star", "Daily log", 10, LocalDate.of(2026, 9, 20))), "NDA="));

        mvc.perform(get("/api/v1/rewards/history").param("limit", "20").param("cursor", "MjA="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entries[0].delta").value(10))
                .andExpect(jsonPath("$.data.nextCursor").value("NDA="));
    }

    @Test
    void historyWithoutPagingParametersUsesServiceDefaults() throws Exception {
        when(rewardsReadService.getHistory(USER_ID, null, null)).thenReturn(new PointsHistoryPageResponse(List.of(), null));

        mvc.perform(get("/api/v1/rewards/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entries.length()").value(0));
    }

    @Test
    void badgesReturnsEarnedAndUnearned() throws Exception {
        when(badgeReadService.getBadgesSyncingNewlyEarned(USER_ID)).thenReturn(List.of(
                new BadgeResponse("first_flow", "First Flow", true, null),
                new BadgeResponse("night_owl", "Night Owl", false, null)));

        mvc.perform(get("/api/v1/rewards/badges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].earned").value(true))
                .andExpect(jsonPath("$.data[1].earned").value(false));
    }

    @Test
    void challengesReturnsProgress() throws Exception {
        when(challengeReadService.getChallenges(USER_ID))
                .thenReturn(List.of(new ChallengeResponse("log_5_days", "Log 5 days", 50, 3, 5, false, false)));

        mvc.perform(get("/api/v1/rewards/challenges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].done").value(3));
    }

    @Test
    void claimChallengeAwardsTheReward() throws Exception {
        when(challengeWriteService.claim(USER_ID, "streak_7")).thenReturn(new ChallengeClaimResponse(50, 670));

        mvc.perform(post("/api/v1/rewards/challenges/streak_7/claim"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pointsAwarded").value(50));
    }

    @Test
    void claimingAnIncompleteChallengeIs409() throws Exception {
        when(challengeWriteService.claim(USER_ID, "streak_7")).thenThrow(new ApiException(ErrorCode.NOT_YET_COMPLETE, "Not complete"));

        mvc.perform(post("/api/v1/rewards/challenges/streak_7/claim"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.data.errorCode").value("not_yet_complete"));
    }
}
