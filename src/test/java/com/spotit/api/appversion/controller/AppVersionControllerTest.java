package com.spotit.api.appversion.controller;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppVersionControllerTest {
    @Mock ConfigurationDomainService configurationDomainService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new AppVersionController(configurationDomainService));
    }

    @Test
    void returnsTheMinimumBuildAndStorePageForEachPlatform() throws Exception {
        when(configurationDomainService.getAppMinBuildAndroid()).thenReturn(12L);
        when(configurationDomainService.getAppStoreUrlAndroid()).thenReturn("https://play.google.com/store/apps/details?id=com.spotit.app");
        when(configurationDomainService.getAppMinBuildIos()).thenReturn(0L);
        when(configurationDomainService.getAppStoreUrlIos()).thenReturn(null);

        mvc.perform(get("/api/v1/app/version-policy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.android.minBuild").value(12))
                .andExpect(jsonPath("$.data.android.storeUrl").value("https://play.google.com/store/apps/details?id=com.spotit.app"))
                .andExpect(jsonPath("$.data.ios.minBuild").value(0));
    }
}
