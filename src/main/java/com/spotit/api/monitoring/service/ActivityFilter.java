package com.spotit.api.monitoring.service;

import com.spotit.api.common.security.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ActivityFilter extends OncePerRequestFilter {
    private static final Pattern ID = Pattern.compile("/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
    private static final Pattern DATE = Pattern.compile("/\\d{4}-\\d{2}-\\d{2}");

    private final MonitoringService monitoringService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } finally {
            UUID userId = currentUserId();
            if (userId != null) {
                monitoringService.touch(userId);
            }
            if (response.getStatus() >= 500 && request.getRequestURI().startsWith("/api/")) {
                monitoringService.recordFailure(userId, actionOf(request), response.getStatus());
            }
        }
    }

    static String actionOf(HttpServletRequest request) {
        String path = ID.matcher(request.getRequestURI()).replaceAll("/{id}");
        path = DATE.matcher(path).replaceAll("/{date}");
        return request.getMethod() + " " + path;
    }

    public static UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() instanceof SecurityUser user ? user.userId() : null;
    }
}
