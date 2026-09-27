package com.spotit.api.smtp.controller;

import com.spotit.api.smtp.SmtpProperties;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SmtpConfigControllerTest {

    private MockMvc mvc(SmtpProperties properties) {
        return mockMvc(new SmtpConfigController(properties));
    }

    @Test
    void statusShowsTheConfiguredRelayButNeverThePassword() throws Exception {
        mvc(new SmtpProperties("smtp.example.com", 587, "mailer", "s3cret", "no-reply@example.com", true))
                .perform(get("/api/v1/config/smtp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configured").value(true))
                .andExpect(jsonPath("$.data.host").value("smtp.example.com"))
                .andExpect(jsonPath("$.data.port").value(587))
                .andExpect(jsonPath("$.data.username").value("mailer"))
                .andExpect(jsonPath("$.data.fromAddress").value("no-reply@example.com"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void statusWhenTheEnvironmentVariablesWereNotEntered() throws Exception {
        mvc(new SmtpProperties("smtp.gmail.com", 587, "", "", "", true))
                .perform(get("/api/v1/config/smtp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configured").value(false))
                .andExpect(jsonPath("$.data.host").doesNotExist());
    }

    @Test
    void theRelayCanNoLongerBeChangedThroughTheApi() throws Exception {
        mvc(new SmtpProperties("smtp.example.com", 587, "mailer", "s3cret", "no-reply@example.com", true))
                .perform(put("/api/v1/config/smtp").contentType(MediaType.APPLICATION_JSON).content("{\"host\":\"evil.example.com\"}"))
                .andExpect(status().isMethodNotAllowed());
    }
}
