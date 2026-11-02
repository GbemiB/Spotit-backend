package com.spotit.api.spotlight.core.controller;

import com.spotit.api.spotlight.core.dto.ChallengeCard;
import com.spotit.api.spotlight.core.dto.RankView;
import com.spotit.api.spotlight.core.dto.RankStep;
import com.spotit.api.spotlight.core.dto.SpotlightHomeResponse;
import com.spotit.api.spotlight.core.dto.SpotlightInvite;
import com.spotit.api.spotlight.core.dto.SpotlightStats;
import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.core.dto.SpotlightChallengeAdmin;
import com.spotit.api.spotlight.core.dto.SpotlightRankAdmin;
import com.spotit.api.spotlight.core.service.SpotlightAdminService;
import com.spotit.api.spotlight.core.service.SpotlightHomeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SpotlightControllerTest {
    @Mock SpotlightHomeService homeService;
    @Mock SpotlightAdminService adminService;

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void theHubShowsStatsChallengesAndInvite() throws Exception {
        MockMvc mvc = mockMvc(new SpotlightController(homeService));
        authenticate();
        when(homeService.home(USER_ID)).thenReturn(new SpotlightHomeResponse(240, new SpotlightStats(3, 5, 2, 40, 6, 2,
                List.of(true, false, true, true, false, true, true), new RankView("curious", "Curious", 0, "Explorer", 15)),
                List.of(new ChallengeCard("pad-factory", "game", "Pad Factory", "Design pads", "pad", "pink", 10, true, 13, "soon", false, 1)),
                new SpotlightInvite(true, 3, 3), List.of(new RankStep("curious", "Curious", 0), new RankStep("explorer", "Explorer", 15))));

        mvc.perform(get("/api/v1/spotlight/home")).andExpect(status().isOk()).andExpect(jsonPath("$.data.balance").value(240))
                .andExpect(jsonPath("$.data.stats.streak").value(3))
                .andExpect(jsonPath("$.data.stats.rank.nextName").value("Explorer"))
                .andExpect(jsonPath("$.data.challenges[0].cost").value(10))
                .andExpect(jsonPath("$.data.challenges[0].freePlaysLeft").value(1))
                .andExpect(jsonPath("$.data.invite.everyDays").value(3))
                .andExpect(jsonPath("$.data.ranks[1].minStars").value(15));
    }

    @Test
    void adminsListAndSaveChallengesAndRanks() throws Exception {
        MockMvc mvc = mockMvc(new SpotlightConfigController(adminService));
        SpotlightChallengeAdmin challenge = new SpotlightChallengeAdmin("pad-factory", "game", "Pad Factory", "Design pads", "pad", "pink", 10, true,
                13, "soon", 2, true, 1);
        SpotlightRankAdmin rank = new SpotlightRankAdmin("explorer", "Explorer", 15, true);
        when(adminService.challenges(null, null)).thenReturn(new AdminPage<>(List.of(challenge), 0, 50, 1));
        when(adminService.saveChallenge(eq("pad-factory"), any())).thenReturn(challenge);
        when(adminService.ranks(null, null)).thenReturn(new AdminPage<>(List.of(rank), 0, 50, 1));
        when(adminService.saveRank(eq("explorer"), any())).thenReturn(rank);

        mvc.perform(get("/api/v1/config/spotlight/challenges")).andExpect(jsonPath("$.data.items[0].cost").value(10));
        mvc.perform(put("/api/v1/config/spotlight/challenges/pad-factory").contentType(MediaType.APPLICATION_JSON).content(json(challenge)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/spotlight/ranks")).andExpect(jsonPath("$.data.items[0].minStars").value(15));
        mvc.perform(put("/api/v1/config/spotlight/ranks/explorer").contentType(MediaType.APPLICATION_JSON).content(json(rank)))
                .andExpect(status().isOk());
    }
}
