package com.spotit.api.insight.controller;

import com.spotit.api.insight.dto.CycleTrendsResponse;
import com.spotit.api.insight.dto.RegularityResponse;
import com.spotit.api.insight.dto.WeeklyDigestResponse;
import com.spotit.api.insight.service.InsightReadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InsightControllerTest {
    @Mock InsightReadService insightReadService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new InsightController(insightReadService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void trendsPassesTheRequestedCycleCount() throws Exception {
        when(insightReadService.getTrends(USER_ID, 3)).thenReturn(new CycleTrendsResponse(List.of(28, 29, 27), 28, 5, 2));

        mvc.perform(get("/api/v1/insights/trends").param("cycles", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cycleLengths.length()").value(3))
                .andExpect(jsonPath("$.data.avgCycleLength").value(28));
    }

    @Test
    void trendsWithoutACountLetsTheServiceApplyItsDefault() throws Exception {
        when(insightReadService.getTrends(USER_ID, null)).thenReturn(new CycleTrendsResponse(List.of(), 28, 5, 0));

        mvc.perform(get("/api/v1/insights/trends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.variationDays").value(0));
    }

    @Test
    void weeklyDigestReturnsTheLastSevenDays() throws Exception {
        when(insightReadService.getWeeklyDigest(USER_ID))
                .thenReturn(new WeeklyDigestResponse(5, "calm", LocalDate.of(2026, 9, 17), LocalDate.of(2026, 9, 23)));

        mvc.perform(get("/api/v1/insights/digest/weekly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.topMood").value("calm"))
                .andExpect(jsonPath("$.data.rangeStart").value("2026-09-17"));
    }

    @Test
    void regularityReturnsStatusAndFlags() throws Exception {
        when(insightReadService.getRegularity(USER_ID))
                .thenReturn(new RegularityResponse("irregular", List.of("variation"), "Not medical advice."));

        mvc.perform(get("/api/v1/insights/regularity"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("irregular"))
                .andExpect(jsonPath("$.data.flags[0]").value("variation"));
    }
}
