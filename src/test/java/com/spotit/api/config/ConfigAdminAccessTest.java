package com.spotit.api.config;

import com.spotit.api.common.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spotit.admin.emails=admin@example.com, second-admin@example.com")
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ConfigAdminAccessTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtService jwtService;

    private String bearer(String email) {
        return "Bearer " + jwtService.generateAccessToken(UUID.randomUUID(), email, false);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/v1/config/products", "/api/v1/config/levels", "/api/v1/config/badges", "/api/v1/config/challenges",
            "/api/v1/config/content", "/api/v1/config/global", "/api/v1/config/smtp"})
    void anOrdinaryUserIsForbidden(String path) throws Exception {
        mockMvc.perform(get(path).header("Authorization", bearer("user@example.com")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.data.errorCode").value("forbidden"));
    }

    @Test
    void anOrdinaryUserCannotChangeASetting() throws Exception {
        mockMvc.perform(patch("/api/v1/config/global/points-daily-claim")
                        .header("Authorization", bearer("user@example.com"))
                        .contentType(MediaType.APPLICATION_JSON).content("{\"value\": 100000}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void noTokenIsStillUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/config/products")).andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin@example.com", "Second-Admin@example.com"})
    void aListedAdminIsAllowed(String email) throws Exception {
        mockMvc.perform(get("/api/v1/config/products").header("Authorization", bearer(email)))
                .andExpect(status().isOk());
    }

    @Test
    void anAdminListDoesNotAffectOrdinaryEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/rewards/levels").header("Authorization", bearer("user@example.com")))
                .andExpect(status().isOk());
    }
}
