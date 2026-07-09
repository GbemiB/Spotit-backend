package com.spotit.api.shop.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductResponse;
import com.spotit.api.shop.dto.RedeemRequest;
import com.spotit.api.shop.dto.RedeemResponse;
import com.spotit.api.shop.service.ShopReadService;
import com.spotit.api.shop.service.ShopWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopReadService shopReadService;
    private final ShopWriteService shopWriteService;

    @GetMapping("/products")
    public List<ProductResponse> products(@CurrentUserId UUID userId) {
        return shopReadService.listProducts(userId);
    }

    @PostMapping("/redeem")
    public RedeemResponse redeem(@CurrentUserId UUID userId, @Valid @RequestBody RedeemRequest request) {
        return shopWriteService.redeem(userId, request.productId());
    }

    @GetMapping("/orders")
    public List<OrderResponse> orders(@CurrentUserId UUID userId) {
        return shopReadService.listOrders(userId);
    }
}
