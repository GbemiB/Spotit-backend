package com.spotit.api.rewards.controller;

import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateChallengeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateChallengeDefinitionRequest;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.ChallengeWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ChallengeConfigControllerTest {
    @Mock ChallengeReadService challengeReadService;
    @Mock ChallengeWriteService challengeWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new ChallengeConfigController(challengeReadService, challengeWriteService));
    }

    private ChallengeDefinitionAdminResponse logFiveDays(int reward) {
        return new ChallengeDefinitionAdminResponse("log_5_days", "Log 5 days", reward, 5, "WEEKLY_LOG");
    }

    @Test
    void listReturnsEveryDefinition() throws Exception {
        when(challengeReadService.listDefinitionsForAdmin()).thenReturn(List.of(logFiveDays(50)));

        mvc.perform(get("/api/v1/config/challenges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].type").value("WEEKLY_LOG"));
    }

    @Test
    void getReturnsOneDefinition() throws Exception {
        when(challengeReadService.getDefinitionForAdmin("log_5_days")).thenReturn(logFiveDays(50));

        mvc.perform(get("/api/v1/config/challenges/log_5_days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(5));
    }

    @Test
    void createReturns201() throws Exception {
        CreateChallengeDefinitionRequest request = new CreateChallengeDefinitionRequest("log_5_days", "Log 5 days", 50, 5, "WEEKLY_LOG");
        when(challengeWriteService.createDefinition(request)).thenReturn(logFiveDays(50));

        mvc.perform(post("/api/v1/config/challenges").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createRejectsAnUnknownType() throws Exception {
        mvc.perform(post("/api/v1/config/challenges").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CreateChallengeDefinitionRequest("x", "X", 50, 5, "MONTHLY"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(challengeWriteService);
    }

    @Test
    void createRejectsAZeroReward() throws Exception {
        mvc.perform(post("/api/v1/config/challenges").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CreateChallengeDefinitionRequest("x", "X", 0, 5, "STATIC"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(challengeWriteService);
    }

    @Test
    void updateChangesTheReward() throws Exception {
        UpdateChallengeDefinitionRequest request = new UpdateChallengeDefinitionRequest(null, 75, null);
        when(challengeWriteService.updateDefinition("log_5_days", request)).thenReturn(logFiveDays(75));

        mvc.perform(patch("/api/v1/config/challenges/log_5_days").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reward").value(75));
    }

    @Test
    void deleteRemovesTheDefinition() throws Exception {
        mvc.perform(delete("/api/v1/config/challenges/log_5_days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Challenge definition deleted."));

        verify(challengeWriteService).deleteDefinition("log_5_days");
    }
}
