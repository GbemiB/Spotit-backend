package com.spotit.api.config;

import com.spotit.api.common.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityRulesTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtService jwtService;
    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    RequestMappingHandlerMapping handlerMapping;

    @Test
    void aMistypedAddressOutsideTheApiIsNotFound() throws Exception {
        mockMvc.perform(post("/api/v1 ç/auth/signup").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.errorCode").value("not_found"));
    }

    @Test
    void anUnknownApiAddressWithoutATokenIsStillUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/does-not-exist")).andExpect(status().isUnauthorized());
    }

    @Test
    void anUnknownApiAddressWithATokenIsNotFound() throws Exception {
        String token = jwtService.generateAccessToken(UUID.randomUUID(), "user@example.com", false);
        mockMvc.perform(get("/api/v1/does-not-exist").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void aProtectedEndpointWithoutATokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/cycle/current"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.data.errorCode").value("unauthorized"));
    }

    @Test
    void aPublicEndpointNeedsNoToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/signup").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void everyControllerLivesUnderTheApiPrefix() {
        var paths = handlerMapping.getHandlerMethods().entrySet().stream()
                .filter(e -> e.getValue().getBeanType().getPackageName().startsWith("com.spotit"))
                .flatMap(e -> e.getKey().getPatternValues().stream())
                .toList();
        assertThat(paths).hasSizeGreaterThan(20).allMatch(path -> path.startsWith("/api/v1/"));
    }

    private String partnerToken() {
        return "Bearer " + jwtService.generateAccessToken(UUID.randomUUID(), "partner@example.com", false, true);
    }

    private String ownerToken() {
        return "Bearer " + jwtService.generateAccessToken(UUID.randomUUID(), "owner@example.com", false, false);
    }

    @Test
    void aPartnerCannotChangeAnythingOfHers() throws Exception {
        String token = partnerToken();
        mockMvc.perform(put("/api/v1/logs/2026-10-22").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Partner accounts can view this but not change it."));
        mockMvc.perform(put("/api/v1/logs/period").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/v1/logs/2026-10-22").header("Authorization", token)).andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/onboarding").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/onboarding/template").header("Authorization", token)).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/users/me/notifications").header("Authorization", token)).andExpect(status().isForbidden());
        mockMvc.perform(patch("/api/v1/users/me/notifications").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/api/v1/users/me/reset").header("Authorization", token)).andExpect(status().isForbidden());
    }

    @Test
    void aPartnerCanReachTheAllowedPaths() throws Exception {
        String token = partnerToken();
        for (String path : new String[]{"/api/v1/partner/status", "/api/v1/partner/profile", "/api/v1/partner/check-in",
                "/api/v1/cycle/current", "/api/v1/cycle/calendar?year=2026&month=10", "/api/v1/logs/2026-10-22",
                "/api/v1/logs?from=2026-10-01&to=2026-10-22", "/api/v1/insights/trends", "/api/v1/insights/regularity",
                "/api/v1/rewards/summary", "/api/v1/shop/products", "/api/v1/billing/subscription", "/api/v1/content/feed",
                "/api/v1/notifications", "/api/v1/users/me", "/api/v1/app/version-policy"}) {
            int code = mockMvc.perform(get(path).header("Authorization", token)).andReturn().getResponse().getStatus();
            assertThat(code).as(path).isNotIn(401, 403);
        }
        int patchCode = mockMvc.perform(patch("/api/v1/users/me").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andReturn().getResponse().getStatus();
        assertThat(patchCode).isNotIn(401, 403);
    }

    @Test
    void anOwnerIsNotAffectedByThePartnerRule() throws Exception {
        String token = ownerToken();
        for (String path : new String[]{"/api/v1/cycle/current", "/api/v1/rewards/summary", "/api/v1/users/me/notifications"}) {
            int code = mockMvc.perform(get(path).header("Authorization", token)).andReturn().getResponse().getStatus();
            assertThat(code).as(path).isNotIn(401, 403);
        }
    }

    @Test
    void withoutATokenTheAnswerIsStillUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/logs/2026-10-22")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/partner/status")).andExpect(status().isUnauthorized());
    }
}
