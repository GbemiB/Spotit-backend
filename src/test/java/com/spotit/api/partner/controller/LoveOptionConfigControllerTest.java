package com.spotit.api.partner.controller;

import com.spotit.api.partner.dto.CreateLoveOptionRequest;
import com.spotit.api.partner.dto.LoveOptionAdminResponse;
import com.spotit.api.partner.dto.UpdateLoveOptionRequest;
import com.spotit.api.partner.service.LoveOptionService;
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
class LoveOptionConfigControllerTest {
    @Mock LoveOptionService loveOptionService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new LoveOptionConfigController(loveOptionService));
    }

    private LoveOptionAdminResponse footRub(String label, boolean active) {
        return new LoveOptionAdminResponse("foot_rub", "promise", label, null, "🦶", null, 7, 7, active);
    }

    @Test
    void listReturnsEveryOption() throws Exception {
        when(loveOptionService.listForAdmin()).thenReturn(List.of(footRub("Foot rub", true)));

        mvc.perform(get("/api/v1/config/love-options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("foot_rub"))
                .andExpect(jsonPath("$.data[0].storedValue").value(7));
    }

    @Test
    void createAddsAnOption() throws Exception {
        CreateLoveOptionRequest request = new CreateLoveOptionRequest("foot_rub", "promise", "Foot rub", null, "🦶", null, 7, true);
        when(loveOptionService.create(request)).thenReturn(footRub("Foot rub", true));

        mvc.perform(post("/api/v1/config/love-options").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.label").value("Foot rub"));
    }

    @Test
    void createRejectsAnUnknownKind() throws Exception {
        CreateLoveOptionRequest request = new CreateLoveOptionRequest("foot_rub", "gift", "Foot rub", null, null, null, null, null);

        mvc.perform(post("/api/v1/config/love-options").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());
        verifyNoInteractions(loveOptionService);
    }

    @Test
    void updateChangesTheOption() throws Exception {
        UpdateLoveOptionRequest request = new UpdateLoveOptionRequest("Foot massage", null, null, null, null, null);
        when(loveOptionService.update("foot_rub", request)).thenReturn(footRub("Foot massage", true));

        mvc.perform(patch("/api/v1/config/love-options/foot_rub").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.label").value("Foot massage"));
    }

    @Test
    void deleteSwitchesTheOptionOff() throws Exception {
        mvc.perform(delete("/api/v1/config/love-options/foot_rub"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Love option switched off."));

        verify(loveOptionService).switchOff("foot_rub");
    }
}
