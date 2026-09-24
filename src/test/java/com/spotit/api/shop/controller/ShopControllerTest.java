package com.spotit.api.shop.controller;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductResponse;
import com.spotit.api.shop.dto.RedeemRequest;
import com.spotit.api.shop.dto.RedeemResponse;
import com.spotit.api.shop.service.ShopReadService;
import com.spotit.api.shop.service.ShopWriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShopControllerTest {
    @Mock ShopReadService shopReadService;
    @Mock ShopWriteService shopWriteService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new ShopController(shopReadService, shopWriteService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void productsShowsLockStateForTheCurrentUser() throws Exception {
        when(shopReadService.listProducts(USER_ID)).thenReturn(List.of(
                new ProductResponse("rosewater_mist", "Rosewater Face Mist", 800, "Rosé", false, true, "level_too_low")));

        mvc.perform(get("/api/v1/shop/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].locked").value(true))
                .andExpect(jsonPath("$.data[0].lockReason").value("level_too_low"));
    }

    @Test
    void redeemSpendsPointsOnTheProduct() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(shopWriteService.redeem(USER_ID, "rosewater_mist"))
                .thenReturn(new RedeemResponse(orderId, "rosewater_mist", 800, 200, "pending"));

        mvc.perform(post("/api/v1/shop/redeem").contentType(MediaType.APPLICATION_JSON).content(json(new RedeemRequest("rosewater_mist"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.data.newBalance").value(200));
    }

    @Test
    void redeemRequiresAProductId() throws Exception {
        mvc.perform(post("/api/v1/shop/redeem").contentType(MediaType.APPLICATION_JSON).content(json(new RedeemRequest(" "))))
                .andExpect(status().isUnprocessableEntity());

        verifyNoInteractions(shopWriteService);
    }

    @Test
    void redeemWithoutEnoughPointsIs402() throws Exception {
        when(shopWriteService.redeem(USER_ID, "rosewater_mist"))
                .thenThrow(new ApiException(ErrorCode.INSUFFICIENT_POINTS, "Not enough points"));

        mvc.perform(post("/api/v1/shop/redeem").contentType(MediaType.APPLICATION_JSON).content(json(new RedeemRequest("rosewater_mist"))))
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.data.errorCode").value("insufficient_points"));
    }

    @Test
    void ordersListsTheUsersOrders() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(shopReadService.listOrders(USER_ID)).thenReturn(List.of(
                new OrderResponse(orderId, "rosewater_mist", "pending", Instant.parse("2026-09-20T10:00:00Z"))));

        mvc.perform(get("/api/v1/shop/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].status").value("pending"))
                .andExpect(jsonPath("$.data[0].createdAt").value("2026-09-20T10:00:00Z"));
    }
}
