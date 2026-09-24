package com.spotit.api.rewards.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;
import com.spotit.api.rewards.service.BadgeReadService;
import com.spotit.api.rewards.service.BadgeWriteService;
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
class BadgeConfigControllerTest {
    @Mock BadgeReadService badgeReadService;
    @Mock BadgeWriteService badgeWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new BadgeConfigController(badgeReadService, badgeWriteService));
    }

    private BadgeDefinitionAdminResponse nightOwl(String name) {
        return new BadgeDefinitionAdminResponse("night_owl", name, "Log after midnight");
    }

    @Test
    void listReturnsEveryDefinition() throws Exception {
        when(badgeReadService.listDefinitionsForAdmin()).thenReturn(List.of(nightOwl("Night Owl")));

        mvc.perform(get("/api/v1/config/badges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("night_owl"));
    }

    @Test
    void getReturnsOneDefinition() throws Exception {
        when(badgeReadService.getDefinitionForAdmin("night_owl")).thenReturn(nightOwl("Night Owl"));

        mvc.perform(get("/api/v1/config/badges/night_owl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Night Owl"));
    }

    @Test
    void createReturns201() throws Exception {
        CreateBadgeDefinitionRequest request = new CreateBadgeDefinitionRequest("night_owl", "Night Owl", "Log after midnight");
        when(badgeWriteService.createDefinition(request)).thenReturn(nightOwl("Night Owl"));

        mvc.perform(post("/api/v1/config/badges").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createRejectsABlankId() throws Exception {
        mvc.perform(post("/api/v1/config/badges").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CreateBadgeDefinitionRequest("", "Night Owl", "desc"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(badgeWriteService);
    }

    @Test
    void createWithADuplicateIdIs409() throws Exception {
        when(badgeWriteService.createDefinition(any())).thenThrow(new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, "Exists"));

        mvc.perform(post("/api/v1/config/badges").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new CreateBadgeDefinitionRequest("night_owl", "Night Owl", "desc"))))
                .andExpect(status().isConflict());
    }

    @Test
    void updateRenamesTheBadge() throws Exception {
        UpdateBadgeDefinitionRequest request = new UpdateBadgeDefinitionRequest("Midnight Logger", null);
        when(badgeWriteService.updateDefinition("night_owl", request)).thenReturn(nightOwl("Midnight Logger"));

        mvc.perform(patch("/api/v1/config/badges/night_owl").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Midnight Logger"));
    }

    @Test
    void deleteRemovesTheDefinition() throws Exception {
        mvc.perform(delete("/api/v1/config/badges/night_owl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Badge definition deleted."));

        verify(badgeWriteService).deleteDefinition("night_owl");
    }
}
