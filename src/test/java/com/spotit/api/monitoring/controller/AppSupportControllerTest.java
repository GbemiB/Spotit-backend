package com.spotit.api.monitoring.controller;

import com.spotit.api.monitoring.dto.CrashReportRequest;
import com.spotit.api.monitoring.service.MonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppSupportControllerTest {
    @Mock MonitoringService monitoringService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new AppSupportController(monitoringService));
    }

    @Test
    void aCrashIsRecorded() throws Exception {
        CrashReportRequest request = new CrashReportRequest("TypeError", "at Home", "Home", "1.1.0", "ios", "iPhone 15", "18.1", null);

        mvc.perform(post("/api/v1/app/crashes").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Crash recorded."));

        verify(monitoringService).recordCrash(isNull(), any(CrashReportRequest.class));
    }

    @Test
    void aCrashNeedsAMessage() throws Exception {
        CrashReportRequest request = new CrashReportRequest(" ", null, null, null, null, null, null, null);

        mvc.perform(post("/api/v1/app/crashes").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());
        verifyNoInteractions(monitoringService);
    }

    @Test
    void theSupportEmailIsPublic() throws Exception {
        when(monitoringService.supportEmail()).thenReturn("spotitinternational@gmail.com");

        mvc.perform(get("/api/v1/app/support"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("spotitinternational@gmail.com"));
    }
}
