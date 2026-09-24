package com.spotit.api.log.controller;

import com.spotit.api.log.dto.LogEntryResponse;
import com.spotit.api.log.dto.LogPeriodRequest;
import com.spotit.api.log.dto.LogPeriodResponse;
import com.spotit.api.log.dto.LogsRangeResponse;
import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;
import com.spotit.api.log.service.LogReadService;
import com.spotit.api.log.service.LogWriteService;
import com.spotit.api.rewards.service.ChallengeReadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {
    @Mock LogReadService logReadService;
    @Mock LogWriteService logWriteService;
    @Mock ChallengeReadService challengeReadService;

    MockMvc mvc;
    LocalDate date = LocalDate.of(2026, 7, 10);

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new LogController(logReadService, logWriteService, challengeReadService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    private LogEntryResponse entry() {
        return new LogEntryResponse(date, "medium", "calm", List.of("cramps"), "note", false);
    }

    @Test
    void templateListsEveryOptionAndTheDailyReward() throws Exception {
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        mvc.perform(get("/api/v1/logs/template"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.flow.length()").value(4))
                .andExpect(jsonPath("$.data.flow[0].id").value("spotting"))
                .andExpect(jsonPath("$.data.mood.length()").value(8))
                .andExpect(jsonPath("$.data.symptoms.length()").value(16))
                .andExpect(jsonPath("$.data.basePoints").value(10));
    }

    @Test
    void saveLogStoresTheEntryForThatDate() throws Exception {
        SaveLogRequest request = new SaveLogRequest("medium", "calm", List.of("cramps"), "note", false);
        when(logWriteService.saveLog(USER_ID, date, request))
                .thenReturn(new SaveLogResponse(date, "medium", "calm", List.of("cramps"), "note", false, 10, 110, 3, true));

        mvc.perform(put("/api/v1/logs/2026-07-10").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pointsAwarded").value(10))
                .andExpect(jsonPath("$.data.streak").value(3));
    }

    @Test
    void saveLogRejectsAnUnknownSymptom() throws Exception {
        SaveLogRequest request = new SaveLogRequest(null, null, List.of("cramps", "sneezing"), null, false);

        mvc.perform(put("/api/v1/logs/2026-07-10").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(logWriteService);
    }

    @Test
    void saveLogRejectsAnUnknownFlow() throws Exception {
        SaveLogRequest request = new SaveLogRequest("torrential", null, List.of(), null, false);

        mvc.perform(put("/api/v1/logs/2026-07-10").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(logWriteService);
    }

    @Test
    void saveLogWithAMalformedDateIs400() throws Exception {
        mvc.perform(put("/api/v1/logs/10-07-2026").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new SaveLogRequest(null, null, List.of(), null, false))))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(logWriteService);
    }

    @Test
    void logPeriodRecordsTheRange() throws Exception {
        LogPeriodRequest request = new LogPeriodRequest(date, date.plusDays(4), null, "heavy", null, List.of(), null, false);
        when(logWriteService.logPeriod(USER_ID, request)).thenReturn(new LogPeriodResponse(date, date.plusDays(4), "heavy", date,
                28, 5, entry(), 20, 120, 4, List.of()));

        mvc.perform(put("/api/v1/logs/period").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.endDate").value("2026-07-14"))
                .andExpect(jsonPath("$.data.pointsAwarded").value(20));
    }

    @Test
    void logPeriodRequiresBothDates() throws Exception {
        LogPeriodRequest request = new LogPeriodRequest(date, null, null, null, null, null, null, false);

        mvc.perform(put("/api/v1/logs/period").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(logWriteService);
    }

    @Test
    void getLogReturnsTheEntry() throws Exception {
        when(logReadService.getLog(USER_ID, date)).thenReturn(entry());

        mvc.perform(get("/api/v1/logs/2026-07-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.flow").value("medium"))
                .andExpect(jsonPath("$.data.symptoms[0]").value("cramps"));
    }

    @Test
    void getLogsInRangeReturnsEntriesKeyedByDate() throws Exception {
        LocalDate from = LocalDate.of(2026, 7, 1);
        when(logReadService.getLogsInRange(USER_ID, from, date)).thenReturn(new LogsRangeResponse(Map.of("2026-07-10", entry())));

        mvc.perform(get("/api/v1/logs").param("from", "2026-07-01").param("to", "2026-07-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.logs['2026-07-10'].mood").value("calm"));
    }

    @Test
    void getLogsInRangeRequiresBothBounds() throws Exception {
        mvc.perform(get("/api/v1/logs").param("from", "2026-07-01"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(logReadService);
    }

    @Test
    void deleteLogRemovesThatDaysEntry() throws Exception {
        mvc.perform(delete("/api/v1/logs/2026-07-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Entry deleted."));

        verify(logWriteService).deleteLog(USER_ID, date);
    }
}
