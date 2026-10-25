package com.spotit.api.monitoring.controller;

import com.spotit.api.monitoring.dto.CrashPage;
import com.spotit.api.monitoring.dto.ReportSummaryResponse;
import com.spotit.api.monitoring.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {
    @Mock ReportService reportService;

    MockMvc mvc;
    LocalDate from = LocalDate.of(2026, 10, 1);
    LocalDate to = LocalDate.of(2026, 10, 31);

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new ReportController(reportService));
    }

    @Test
    void theSummaryIsReturned() throws Exception {
        when(reportService.summary(from, to, 0, 50)).thenReturn(new ReportSummaryResponse(from, to,
                new ReportSummaryResponse.Downloads(5, 4, 1, Map.of("android", 4L)), 6,
                new ReportSummaryResponse.Active(2, 3, 4), new ReportSummaryResponse.Crashes(0, List.of()),
                new ReportSummaryResponse.Failures(0, Map.of()), new ReportSummaryResponse.LastActivityPage(0, 50, 0, List.of())));

        mvc.perform(get("/api/v1/config/reports/summary").param("from", "2026-10-01").param("to", "2026-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.downloads.total").value(5))
                .andExpect(jsonPath("$.data.active.weekly").value(3));
    }

    @Test
    void theCrashListIsReturned() throws Exception {
        when(reportService.crashes(from, to, 1, 20)).thenReturn(new CrashPage(1, 20, 0, List.of()));

        mvc.perform(get("/api/v1/config/reports/crashes").param("from", "2026-10-01").param("to", "2026-10-31")
                        .param("page", "1").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page").value(1));
    }
}
