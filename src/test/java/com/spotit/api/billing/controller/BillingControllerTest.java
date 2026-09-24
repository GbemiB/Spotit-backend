package com.spotit.api.billing.controller;

import com.spotit.api.billing.dto.CancelResponse;
import com.spotit.api.billing.dto.RestoreRequest;
import com.spotit.api.billing.dto.SubscribeRequest;
import com.spotit.api.billing.dto.SubscriptionResponse;
import com.spotit.api.billing.service.BillingReadService;
import com.spotit.api.billing.service.BillingWriteService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

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
class BillingControllerTest {
    @Mock BillingReadService billingReadService;
    @Mock BillingWriteService billingWriteService;

    MockMvc mvc;
    Instant renewsAt = Instant.parse("2026-10-24T00:00:00Z");

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new BillingController(billingReadService, billingWriteService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void statusReturnsTheCurrentUsersSubscription() throws Exception {
        when(billingReadService.getStatus(USER_ID)).thenReturn(new SubscriptionResponse(true, "monthly", renewsAt, true));

        mvc.perform(get("/api/v1/billing/subscription"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.plan").value("monthly"))
                .andExpect(jsonPath("$.data.renewsAt").value("2026-10-24T00:00:00Z"));
    }

    @Test
    void subscribeActivatesPremium() throws Exception {
        SubscribeRequest request = new SubscribeRequest("monthly", "ios", "receipt-data");
        when(billingWriteService.subscribe(USER_ID, request)).thenReturn(new SubscriptionResponse(true, "monthly", renewsAt, true));

        mvc.perform(post("/api/v1/billing/subscription").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.autoRenew").value(true));
    }

    @Test
    void subscribeRejectsAnUnknownPlatform() throws Exception {
        mvc.perform(post("/api/v1/billing/subscription").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new SubscribeRequest("monthly", "windows", "receipt-data"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.data.errorCode").value("validation_error"));

        verifyNoInteractions(billingWriteService);
    }

    @Test
    void subscribeWhenAlreadyPremiumIs409() throws Exception {
        when(billingWriteService.subscribe(eq(USER_ID), any()))
                .thenThrow(new ApiException(ErrorCode.ALREADY_SUBSCRIBED, "Already subscribed"));

        mvc.perform(post("/api/v1/billing/subscription").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new SubscribeRequest("monthly", "android", "receipt-data"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.data.errorCode").value("already_subscribed"));
    }

    @Test
    void cancelTurnsOffAutoRenew() throws Exception {
        when(billingWriteService.cancel(USER_ID)).thenReturn(new CancelResponse(true, false, renewsAt));

        mvc.perform(post("/api/v1/billing/subscription/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.autoRenew").value(false))
                .andExpect(jsonPath("$.data.accessUntil").value("2026-10-24T00:00:00Z"));
    }

    @Test
    void restoreReappliesAPreviousPurchase() throws Exception {
        RestoreRequest request = new RestoreRequest("android", "receipt-data");
        when(billingWriteService.restore(USER_ID, request)).thenReturn(new SubscriptionResponse(true, "yearly", renewsAt, true));

        mvc.perform(post("/api/v1/billing/restore").contentType(MediaType.APPLICATION_JSON).content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.plan").value("yearly"));
    }

    @Test
    void restoreRequiresAReceipt() throws Exception {
        mvc.perform(post("/api/v1/billing/restore").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new RestoreRequest("android", ""))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(billingWriteService);
    }
}
