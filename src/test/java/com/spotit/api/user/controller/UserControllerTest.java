package com.spotit.api.user.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.user.dto.ExportDataResponse;
import com.spotit.api.user.dto.ExportJobResponse;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.dto.ResetResponse;
import com.spotit.api.user.dto.UpdateNotificationPrefsRequest;
import com.spotit.api.user.dto.UpdateProfileRequest;
import com.spotit.api.user.dto.UserResponse;
import com.spotit.api.user.service.UserReadService;
import com.spotit.api.user.service.UserWriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock UserReadService userReadService;
    @Mock UserWriteService userWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new UserController(userReadService, userWriteService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    private UserResponse profile(String firstName) {
        return new UserResponse(USER_ID, firstName, "Doe", "jane@example.com", LocalDate.of(1998, 4, 12), "track",
                28, 5, LocalDate.of(2026, 9, 1), "system", true, false);
    }

    @Test
    void getProfileReturnsTheCurrentUser() throws Exception {
        when(userReadService.getProfile(USER_ID)).thenReturn(profile("Jane"));

        mvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(USER_ID.toString()))
                .andExpect(jsonPath("$.data.dob").value("1998-04-12"));
    }

    @Test
    void updateProfileAppliesThePatch() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest("Janet", null, null, null, "dark");
        when(userWriteService.updateProfile(USER_ID, request)).thenReturn(profile("Janet"));

        mvc.perform(patch("/api/v1/users/me").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("Janet"));
    }

    @Test
    void updateProfileRejectsAnUnknownTheme() throws Exception {
        mvc.perform(patch("/api/v1/users/me").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new UpdateProfileRequest(null, null, null, null, "neon"))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(userWriteService);
    }

    @Test
    void updateProfileRejectsAPeriodLengthOutsideTheSupportedRange() throws Exception {
        mvc.perform(patch("/api/v1/users/me").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new UpdateProfileRequest(null, null, null, 11, null))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(userWriteService);
    }

    @Test
    void getNotificationsReturnsThePreferences() throws Exception {
        when(userReadService.getNotificationPrefs(USER_ID)).thenReturn(new NotificationPrefsResponse(true, false, true, false));

        mvc.perform(get("/api/v1/users/me/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.period").value(true))
                .andExpect(jsonPath("$.data.ovulation").value(false));
    }

    @Test
    void updateNotificationsAppliesThePatch() throws Exception {
        UpdateNotificationPrefsRequest request = new UpdateNotificationPrefsRequest(null, true, null, null);
        when(userWriteService.updateNotificationPrefs(USER_ID, request)).thenReturn(new NotificationPrefsResponse(true, true, true, false));

        mvc.perform(patch("/api/v1/users/me/notifications").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ovulation").value(true));
    }

    @Test
    void exportCreatesAJob() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(userWriteService.requestExport(USER_ID)).thenReturn(new ExportJobResponse(jobId, "ready"));

        mvc.perform(post("/api/v1/users/me/export"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.jobId").value(jobId.toString()))
                .andExpect(jsonPath("$.data.status").value("ready"));
    }

    @Test
    void downloadExportReturnsTheData() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(userReadService.getExportData(USER_ID, jobId)).thenReturn(new ExportDataResponse(profile("Jane"),
                List.of(Map.of("date", "2026-09-01", "flow", "heavy")), List.of()));

        mvc.perform(get("/api/v1/users/me/export/{jobId}/download", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profile.email").value("jane@example.com"))
                .andExpect(jsonPath("$.data.logs[0].flow").value("heavy"));
    }

    @Test
    void downloadingSomeoneElsesExportIs404() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(userReadService.getExportData(USER_ID, jobId)).thenThrow(new ApiException(ErrorCode.NOT_FOUND, "Export not found"));

        mvc.perform(get("/api/v1/users/me/export/{jobId}/download", jobId))
                .andExpect(status().isNotFound());
    }

    @Test
    void resetWipesTheUsersData() throws Exception {
        when(userWriteService.resetAllData(USER_ID)).thenReturn(new ResetResponse("All data reset.", Instant.parse("2026-09-24T12:00:00Z")));

        mvc.perform(post("/api/v1/users/me/reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("All data reset."))
                .andExpect(jsonPath("$.data.resetAt").value("2026-09-24T12:00:00Z"));
    }
}
