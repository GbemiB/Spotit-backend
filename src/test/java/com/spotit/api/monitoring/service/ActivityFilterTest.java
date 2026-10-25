package com.spotit.api.monitoring.service;

import com.spotit.api.common.security.SecurityUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActivityFilterTest {
    @Mock MonitoringService monitoringService;
    @InjectMocks ActivityFilter filter;

    UUID userId = UUID.randomUUID();

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private void signIn() {
        SecurityUser user = new SecurityUser(userId, "a@b.com", false, false);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, List.of()));
    }

    @Test
    void aSignedInRequestMarksTheUserActive() throws Exception {
        signIn();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(new MockHttpServletRequest("GET", "/api/v1/cycle/current"), response, (req, res) -> { });

        verify(monitoringService).touch(userId);
        verify(monitoringService, never()).recordFailure(any(), any(), anyInt());
    }

    @Test
    void aServerErrorIsRecordedWithIdsAndDatesHidden() throws Exception {
        signIn();
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE",
                "/api/v1/logs/period/2026-10-07/" + UUID.randomUUID());

        filter.doFilter(request, new MockHttpServletResponse(), (req, res) -> ((MockHttpServletResponse) res).setStatus(500));

        verify(monitoringService).recordFailure(userId, "DELETE /api/v1/logs/period/{date}/{id}", 500);
    }

    @Test
    void anonymousAndNonApiRequestsAreLeftAlone() throws Exception {
        filter.doFilter(new MockHttpServletRequest("GET", "/privacy.html"), new MockHttpServletResponse(),
                (req, res) -> ((MockHttpServletResponse) res).setStatus(500));

        verify(monitoringService, never()).touch(any());
        verify(monitoringService, never()).recordFailure(any(), any(), anyInt());
        assertThat(ActivityFilter.currentUserId()).isNull();
    }
}
