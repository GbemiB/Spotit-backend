package com.spotit.api.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spotit.api.common.exception.GlobalExceptionHandler;
import com.spotit.api.common.security.CurrentUserIdArgumentResolver;
import com.spotit.api.common.security.SecurityUser;
import com.spotit.api.common.web.ApiResponseAdvice;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

/**
 * Standalone MockMvc wiring shared by the controller tests: the real @CurrentUserId resolver, exception
 * handler and ApiResponse envelope advice around a controller whose services are mocks — so each test
 * pins routing, validation, status codes and the JSON envelope without booting the application context.
 */
public final class ControllerTestSupport {
    public static final UUID USER_ID = UUID.fromString("7c9e6679-7425-40de-944b-e07fc1f90ae7");

    // Mirrors Spring Boot's auto-configured mapper: java.time values serialise as ISO-8601 strings.
    private static final ObjectMapper JSON = Jackson2ObjectMapperBuilder.json()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    private ControllerTestSupport() {
    }

    public static MockMvc mockMvc(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new CurrentUserIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler(), new ApiResponseAdvice())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(JSON))
                .build();
    }

    /** Puts USER_ID into the SecurityContext, as JwtAuthenticationFilter would for a valid bearer token. */
    public static void authenticate() {
        SecurityUser user = new SecurityUser(USER_ID, "jane@example.com", false);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    public static String json(Object body) {
        try {
            return JSON.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
