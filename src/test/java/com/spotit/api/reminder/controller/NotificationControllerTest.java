package com.spotit.api.reminder.controller;

import com.spotit.api.reminder.dto.NotificationInboxResponse;
import com.spotit.api.reminder.dto.NotificationItemResponse;
import com.spotit.api.reminder.service.ReminderReadService;
import com.spotit.api.reminder.service.ReminderWriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {
    @Mock ReminderReadService reminderReadService;
    @Mock ReminderWriteService reminderWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new NotificationController(reminderReadService, reminderWriteService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void inboxReturnsTheCurrentUsersNotifications() throws Exception {
        when(reminderReadService.getInbox(USER_ID)).thenReturn(new NotificationInboxResponse(List.of(new NotificationItemResponse(
                UUID.randomUUID(), "period_upcoming", "Period reminder", "Your period is predicted to start in 3 days.", "home",
                Instant.parse("2026-10-30T08:00:00Z"), false)), 1));

        mvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.unreadCount").value(1))
                .andExpect(jsonPath("$.data.items[0].type").value("period_upcoming"))
                .andExpect(jsonPath("$.data.items[0].body").value("Your period is predicted to start in 3 days."))
                .andExpect(jsonPath("$.data.items[0].screen").value("home"))
                .andExpect(jsonPath("$.data.items[0].sentAt").value("2026-10-30T08:00:00Z"))
                .andExpect(jsonPath("$.data.items[0].read").value(false));
    }

    @Test
    void markReadMarksTheCurrentUsersNotifications() throws Exception {
        mvc.perform(post("/api/v1/notifications/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Notifications marked as read."));

        verify(reminderWriteService).markAllRead(USER_ID);
    }
}
