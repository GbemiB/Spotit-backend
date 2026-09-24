package com.spotit.api.user.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.user.dto.OnboardingRequest;
import com.spotit.api.user.dto.OnboardingResponse;
import com.spotit.api.user.service.UserWriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OnboardingControllerTest {
    @Mock UserWriteService userWriteService;

    MockMvc mvc;
    LocalDate dob = LocalDate.of(1998, 4, 12);

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new OnboardingController(userWriteService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void templateListsEveryGoalWithLocalisedLabels() throws Exception {
        mvc.perform(get("/api/v1/onboarding/template"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.goals.length()").value(4))
                .andExpect(jsonPath("$.data.goals[0].id").value("track"))
                .andExpect(jsonPath("$.data.goals[0].label").value("Track my cycle"))
                .andExpect(jsonPath("$.data.goals[0].description").value("Understand your body and patterns"));
    }

    @Test
    void completeOnboardsTheCurrentUser() throws Exception {
        OnboardingRequest request = new OnboardingRequest(dob, LocalDate.of(2026, 9, 1), "track", 29, 5);
        when(userWriteService.completeOnboarding(USER_ID, request)).thenReturn(new OnboardingResponse(true, 29, 5, "track"));

        mvc.perform(post("/api/v1/onboarding/complete").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.onboarded").value(true))
                .andExpect(jsonPath("$.data.cycleLength").value(29));
    }

    @Test
    void completeRejectsACycleLengthOutsideTheSupportedRange() throws Exception {
        OnboardingRequest request = new OnboardingRequest(dob, null, "track", 60, null);

        mvc.perform(post("/api/v1/onboarding/complete").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(userWriteService);
    }

    @Test
    void completeRequiresADateOfBirth() throws Exception {
        OnboardingRequest request = new OnboardingRequest(null, null, "track", null, null);

        mvc.perform(post("/api/v1/onboarding/complete").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(userWriteService);
    }

    @Test
    void completeWithAnUnknownGoalIs422() throws Exception {
        when(userWriteService.completeOnboarding(eq(USER_ID), any())).thenThrow(new ApiException(ErrorCode.INVALID_GOAL, "Unknown goal"));

        mvc.perform(post("/api/v1/onboarding/complete").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new OnboardingRequest(dob, null, "win", null, null))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.data.errorCode").value("invalid_goal"));
    }
}
