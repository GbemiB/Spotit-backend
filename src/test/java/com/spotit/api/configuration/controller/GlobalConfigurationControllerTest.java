package com.spotit.api.configuration.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalConfigurationControllerTest {
    @Mock ConfigurationDomainService configurationDomainService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new GlobalConfigurationController(configurationDomainService));
    }

    private GlobalConfigurationResponse cycleLength(long value) {
        return new GlobalConfigurationResponse(UUID.randomUUID(), "cycle-default-length", "cycle", true, value, null, null, "Default cycle length");
    }

    @Test
    void listReturnsEveryProperty() throws Exception {
        when(configurationDomainService.listAll()).thenReturn(List.of(cycleLength(28)));

        mvc.perform(get("/api/v1/config/global"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("cycle-default-length"))
                .andExpect(jsonPath("$.data[0].value").value(28));
    }

    @Test
    void listGroupsReturnsGroupNames() throws Exception {
        when(configurationDomainService.listGroupNames()).thenReturn(List.of("cycle", "smtp"));

        mvc.perform(get("/api/v1/config/global/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[1]").value("smtp"));
    }

    @Test
    void listByGroupFiltersToThatGroup() throws Exception {
        when(configurationDomainService.listByGroup("cycle")).thenReturn(List.of(cycleLength(28)));

        mvc.perform(get("/api/v1/config/global/group/cycle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void getReturnsOneProperty() throws Exception {
        when(configurationDomainService.getByName("cycle-default-length")).thenReturn(cycleLength(28));

        mvc.perform(get("/api/v1/config/global/cycle-default-length"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.groupName").value("cycle"));
    }

    @Test
    void getAnUnknownPropertyIs404() throws Exception {
        when(configurationDomainService.getByName("nope")).thenThrow(new ApiException(ErrorCode.NOT_FOUND, "Unknown property"));

        mvc.perform(get("/api/v1/config/global/nope"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.errorCode").value("not_found"));
    }

    @Test
    void updateAppliesThePartialChange() throws Exception {
        UpdateGlobalConfigurationRequest request = new UpdateGlobalConfigurationRequest(null, null, 30L, null, null, null);
        when(configurationDomainService.update("cycle-default-length", request)).thenReturn(cycleLength(30));

        mvc.perform(patch("/api/v1/config/global/cycle-default-length").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.value").value(30));
    }
}
