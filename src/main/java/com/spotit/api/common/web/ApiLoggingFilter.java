package com.spotit.api.common.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Logs every API call's method, URL, request body, response status, and
 * response body at INFO level. Wraps the request/response in Spring's
 * content-caching wrappers since servlet streams can only be read once —
 * {@link ContentCachingResponseWrapper#copyBodyToResponse()} must run after
 * logging or the client would receive an empty body. Ordered ahead of
 * everything (including the Spring Security chain) so auth failures and
 * their real status codes get logged too.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final Pattern SENSITIVE_FIELD = Pattern.compile(
            "(\"(?:password|newPassword|receipt|pushToken|accessToken|refreshToken)\"\\s*:\\s*)\"[^\"]*\"",
            Pattern.CASE_INSENSITIVE);
    private static final int MAX_LOGGED_BODY_CHARS = 2000;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.contains("/api-docs") || path.contains("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            String query = wrappedRequest.getQueryString();
            String url = query == null ? wrappedRequest.getRequestURI() : wrappedRequest.getRequestURI() + "?" + query;
            String requestBody = redact(bodyAsString(wrappedRequest.getContentAsByteArray()));
            String responseBody = redact(bodyAsString(wrappedResponse.getContentAsByteArray()));

            log.info("{} {} -> {} | request={} response={}",
                    wrappedRequest.getMethod(), url, wrappedResponse.getStatus(), requestBody, responseBody);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private String bodyAsString(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        String body = new String(content, StandardCharsets.UTF_8);
        return body.length() > MAX_LOGGED_BODY_CHARS ? body.substring(0, MAX_LOGGED_BODY_CHARS) + "...(truncated)" : body;
    }

    private String redact(String body) {
        if (body == null || body.isEmpty()) {
            return body;
        }
        return SENSITIVE_FIELD.matcher(body).replaceAll("$1\"***\"");
    }
}
