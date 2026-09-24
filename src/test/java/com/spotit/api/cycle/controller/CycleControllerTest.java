package com.spotit.api.cycle.controller;

import com.spotit.api.cycle.dto.CycleCalendarResponse;
import com.spotit.api.cycle.dto.CycleCurrentResponse;
import com.spotit.api.cycle.service.CycleReadService;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CycleControllerTest {
    @Mock CycleReadService cycleReadService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new CycleController(cycleReadService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void currentReturnsTheUsersCycleDay() throws Exception {
        when(cycleReadService.getCurrent(USER_ID))
                .thenReturn(new CycleCurrentResponse(12, "fertile", LocalDate.of(2026, 10, 10), 16L, "high"));

        mvc.perform(get("/api/v1/cycle/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cycleDay").value(12))
                .andExpect(jsonPath("$.data.nextPeriodDate").value("2026-10-10"));
    }

    @Test
    void calendarReturnsTheRequestedMonth() throws Exception {
        when(cycleReadService.getCalendarMonth(USER_ID, 2026, 7)).thenReturn(new CycleCalendarResponse(2026, 7,
                List.of(new CycleCalendarResponse.DayPhase("2026-07-01", "period"))));

        mvc.perform(get("/api/v1/cycle/calendar").param("year", "2026").param("month", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.days[0].phase").value("period"));
    }

    @Test
    void calendarWithoutAMonthIs400() throws Exception {
        mvc.perform(get("/api/v1/cycle/calendar").param("year", "2026"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cycleReadService);
    }
}
