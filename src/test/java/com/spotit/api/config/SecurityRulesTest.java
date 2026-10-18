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
}
