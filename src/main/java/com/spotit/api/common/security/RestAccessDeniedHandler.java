package com.spotit.api.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.api.common.dto.ApiResponse;
import com.spotit.api.common.dto.ErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    static final String DEFAULT_MESSAGE = "You do not have permission to do this.";
    static final String PARTNER_MESSAGE = "Partner accounts can view this but not change it.";

    private final ObjectMapper objectMapper;

    private static boolean isPartner() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> SecurityUser.ROLE_PARTNER.equals(authority.getAuthority()));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        String message = isPartner() ? PARTNER_MESSAGE : DEFAULT_MESSAGE;
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.of(HttpServletResponse.SC_FORBIDDEN, message,
                        new ErrorDetail("forbidden"))));
    }
}
