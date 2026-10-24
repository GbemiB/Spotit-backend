package com.spotit.api.partner.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.partner.dto.AcceptInviteRequest;
import com.spotit.api.partner.dto.InviteRequest;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;
import com.spotit.api.partner.dto.PartnerProfileResponse;
import com.spotit.api.log.dto.EnumOption;
import com.spotit.api.partner.dto.PartnerSettingsRequest;
import com.spotit.api.partner.dto.PartnerStatusResponse;
import com.spotit.api.partner.service.PartnerCheckInService;
import com.spotit.api.partner.service.PartnerReadService;
import com.spotit.api.partner.service.PartnerWriteService;
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

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PartnerControllerTest {
    @Mock PartnerWriteService partnerWriteService;
    @Mock PartnerReadService partnerReadService;
    @Mock PartnerCheckInService partnerCheckInService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new PartnerController(partnerWriteService, partnerReadService, partnerCheckInService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void inviteReturnsThePendingStatus() throws Exception {
        InviteRequest request = new InviteRequest("tunde@example.com");
        when(partnerWriteService.invite(USER_ID, request))
                .thenReturn(PartnerStatusResponse.ownerInvite("pending", "tunde@example.com", Instant.parse("2026-10-24T09:00:00Z")));

        mvc.perform(post("/api/v1/partner/invite").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.state").value("pending"))
                .andExpect(jsonPath("$.data.inviteEmail").value("tunde@example.com"));
    }

    @Test
    void inviteRejectsAnInvalidEmail() throws Exception {
        mvc.perform(post("/api/v1/partner/invite").contentType(MediaType.APPLICATION_JSON).content(json(new InviteRequest("not-an-email"))))
                .andExpect(status().isUnprocessableEntity());
        verifyNoInteractions(partnerWriteService);
    }

    @Test
    void inviteLimitReturns429() throws Exception {
        InviteRequest request = new InviteRequest("tunde@example.com");
        when(partnerWriteService.invite(USER_ID, request))
                .thenThrow(new ApiException(ErrorCode.INVITE_LIMIT_REACHED, ErrorMessage.PARTNER_INVITE_LIMIT_REACHED));

        mvc.perform(post("/api/v1/partner/invite").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value(ErrorMessage.PARTNER_INVITE_LIMIT_REACHED));
    }

    @Test
    void acceptLinksTheCurrentUser() throws Exception {
        AcceptInviteRequest request = new AcceptInviteRequest("K7P2QX", "Amaka");
        when(partnerWriteService.accept(USER_ID, request)).thenReturn(PartnerStatusResponse.partnerActive("Amaka", "Amaka", true, null));

        mvc.perform(post("/api/v1/partner/accept").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value("partner"))
                .andExpect(jsonPath("$.data.nickname").value("Amaka"));
    }

    @Test
    void acceptNeedsANickname() throws Exception {
        mvc.perform(post("/api/v1/partner/accept").contentType(MediaType.APPLICATION_JSON).content(json(new AcceptInviteRequest("K7P2QX", ""))))
                .andExpect(status().isUnprocessableEntity());
        verifyNoInteractions(partnerWriteService);
    }

    @Test
    void anExpiredInviteReturns410() throws Exception {
        AcceptInviteRequest request = new AcceptInviteRequest("K7P2QX", "Amaka");
        when(partnerWriteService.accept(USER_ID, request))
                .thenThrow(new ApiException(ErrorCode.INVITE_EXPIRED, ErrorMessage.PARTNER_INVITE_EXPIRED));

        mvc.perform(post("/api/v1/partner/accept").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isGone());
    }

    @Test
    void statusReturnsTheCurrentState() throws Exception {
        when(partnerReadService.getStatus(USER_ID)).thenReturn(PartnerStatusResponse.ownerNone());

        mvc.perform(get("/api/v1/partner/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.state").value("none"));
    }

    @Test
    void settingsAreSaved() throws Exception {
        PartnerSettingsRequest request = new PartnerSettingsRequest("Babe", false);
        when(partnerWriteService.updateSettings(USER_ID, request)).thenReturn(PartnerStatusResponse.partnerActive("Amaka", "Babe", false, null));

        mvc.perform(patch("/api/v1/partner/settings").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("Babe"))
                .andExpect(jsonPath("$.data.partnerReminder").value(false));
    }

    @Test
    void cancelInviteAndStopSharingReturnMessages() throws Exception {
        mvc.perform(delete("/api/v1/partner/invite"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Invite cancelled."));
        verify(partnerWriteService).cancelInvite(USER_ID);

        mvc.perform(delete("/api/v1/partner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sharing stopped."));
        verify(partnerWriteService).unlink(USER_ID);
    }

    @Test
    void profileReturnsHerNameAndCycleSettings() throws Exception {
        when(partnerReadService.getOwnerProfile(USER_ID)).thenReturn(new PartnerProfileResponse("Amaka", "Babe", 28, 5,
                LocalDate.parse("2026-10-06"), LocalDate.parse("2026-10-01")));

        mvc.perform(get("/api/v1/partner/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("Amaka"))
                .andExpect(jsonPath("$.data.lastPeriodDate").value("2026-10-06"));
    }

    @Test
    void checkInRecordsHowHeSupportedHer() throws Exception {
        PartnerCheckInRequest request = new PartnerCheckInRequest(List.of("snacks"));
        when(partnerCheckInService.checkIn(USER_ID, request)).thenReturn(new PartnerCheckInResponse(List.of(new EnumOption("snacks", "Brought her snacks")),
                true, List.of("snacks"), 10, 2, 10, 120));

        mvc.perform(post("/api/v1/partner/check-in").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pointsAwarded").value(10))
                .andExpect(jsonPath("$.data.streak").value(2));
    }

    @Test
    void checkInNeedsAtLeastOneWay() throws Exception {
        mvc.perform(post("/api/v1/partner/check-in").contentType(MediaType.APPLICATION_JSON).content(json(new PartnerCheckInRequest(List.of()))))
                .andExpect(status().isUnprocessableEntity());
        verifyNoInteractions(partnerCheckInService);
    }

    @Test
    void todaysCheckInCanBeRead() throws Exception {
        when(partnerCheckInService.getToday(USER_ID)).thenReturn(new PartnerCheckInResponse(List.of(), false, List.of(), 10, 0, 0, 0));

        mvc.perform(get("/api/v1/partner/check-in"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.checkedInToday").value(false));
    }
}
