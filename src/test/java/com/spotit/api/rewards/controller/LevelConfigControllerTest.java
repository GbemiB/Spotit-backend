package com.spotit.api.rewards.controller;

import com.spotit.api.rewards.dto.CreateLevelDefinitionRequest;
import com.spotit.api.rewards.dto.LevelDefinitionAdminResponse;
import com.spotit.api.rewards.dto.UpdateLevelDefinitionRequest;
import com.spotit.api.rewards.service.LevelDefinitionService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LevelConfigControllerTest {
    @Mock LevelDefinitionService levelDefinitionService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new LevelConfigController(levelDefinitionService));
    }

    private LevelDefinitionAdminResponse petal(long high) {
        return new LevelDefinitionAdminResponse("petal", "Petal", 500, high, 2);
    }

    @Test
    void listReturnsEveryDefinition() throws Exception {
        when(levelDefinitionService.listDefinitionsForAdmin()).thenReturn(List.of(petal(2000)));

        mvc.perform(get("/api/v1/config/levels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].sortOrder").value(2));
    }

    @Test
    void getReturnsOneDefinition() throws Exception {
        when(levelDefinitionService.getDefinitionForAdmin("petal")).thenReturn(petal(2000));

        mvc.perform(get("/api/v1/config/levels/petal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pointsLow").value(500));
    }

    @Test
    void createReturns201() throws Exception {
        CreateLevelDefinitionRequest request = new CreateLevelDefinitionRequest("petal", "Petal", 500, 2000, 2);
        when(levelDefinitionService.createDefinition(request)).thenReturn(petal(2000));

        mvc.perform(post("/api/v1/config/levels").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createRejectsNegativePoints() throws Exception {
        mvc.perform(post("/api/v1/config/levels").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CreateLevelDefinitionRequest("petal", "Petal", -1, 2000, 2))))
                .andExpect(status().isUnprocessableEntity());

        verify(levelDefinitionService, never()).createDefinition(any());
    }

    @Test
    void updateChangesTheUpperBound() throws Exception {
        UpdateLevelDefinitionRequest request = new UpdateLevelDefinitionRequest(null, null, 2500L, null);
        when(levelDefinitionService.updateDefinition("petal", request)).thenReturn(petal(2500));

        mvc.perform(patch("/api/v1/config/levels/petal").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pointsHigh").value(2500));
    }

    @Test
    void deleteRemovesTheDefinition() throws Exception {
        mvc.perform(delete("/api/v1/config/levels/petal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Level definition deleted."));

        verify(levelDefinitionService).deleteDefinition("petal");
    }
}
