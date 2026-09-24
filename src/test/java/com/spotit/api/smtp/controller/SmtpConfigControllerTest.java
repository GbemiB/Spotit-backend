package com.spotit.api.smtp.controller;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.smtp.dto.SaveSmtpSettingsRequest;
import com.spotit.api.smtp.service.ResolvedSmtpSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SmtpConfigControllerTest {
    @Mock ConfigurationDomainService configurationDomainService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new SmtpConfigController(configurationDomainService));
    }

    private ResolvedSmtpSettings settings() {
        return new ResolvedSmtpSettings("smtp.example.com", 587, "mailer", "s3cret", "no-reply@example.com", true);
    }

    @Test
    void statusShowsTheSettingsButNeverThePassword() throws Exception {
        when(configurationDomainService.getSmtpSettings()).thenReturn(Optional.of(settings()));

        mvc.perform(get("/api/v1/config/smtp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configured").value(true))
                .andExpect(jsonPath("$.data.host").value("smtp.example.com"))
                .andExpect(jsonPath("$.data.port").value(587))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void statusWhenNothingIsConfigured() throws Exception {
        when(configurationDomainService.getSmtpSettings()).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/config/smtp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configured").value(false))
                .andExpect(jsonPath("$.data.host").doesNotExist());
    }

    @Test
    void saveStoresTheSettingsAndReturnsTheNewStatus() throws Exception {
        SaveSmtpSettingsRequest request = new SaveSmtpSettingsRequest("smtp.example.com", 587, "mailer", "s3cret", "no-reply@example.com", true);
        when(configurationDomainService.getSmtpSettings()).thenReturn(Optional.of(settings()));

        mvc.perform(put("/api/v1/config/smtp").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configured").value(true));

        verify(configurationDomainService).saveSmtpSettings("smtp.example.com", 587, "mailer", "s3cret", "no-reply@example.com", true);
    }

    @Test
    void saveRejectsAnOutOfRangePort() throws Exception {
        SaveSmtpSettingsRequest request = new SaveSmtpSettingsRequest("smtp.example.com", 70000, "mailer", null, "no-reply@example.com", true);

        mvc.perform(put("/api/v1/config/smtp").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(configurationDomainService);
    }
}
