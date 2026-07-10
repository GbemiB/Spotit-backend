package com.spotit.api.shop.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.shop.dto.OrderResponse;
import com.spotit.api.shop.dto.ProductResponse;
import com.spotit.api.shop.dto.RedeemRequest;
import com.spotit.api.shop.dto.RedeemResponse;
import com.spotit.api.shop.service.ShopReadService;
import com.spotit.api.shop.service.ShopWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Shop", description = "Rewards-shop product listing, redemption, and order history.")
@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopReadService shopReadService;
    private final ShopWriteService shopWriteService;

    @Operation(summary = ShopControllerSwagger.PRODUCTS_SUMMARY, description = ShopControllerSwagger.PRODUCTS_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products with lock status.", content = @Content(
                    schema = @Schema(implementation = ProductResponse.class),
                    examples = @ExampleObject(value = ShopControllerSwagger.PRODUCTS_200_EXAMPLE)))
    })
    @GetMapping("/products")
    public List<ProductResponse> products(@CurrentUserId UUID userId) {
        return shopReadService.listProducts(userId);
    }

    @Operation(summary = ShopControllerSwagger.REDEEM_SUMMARY, description = ShopControllerSwagger.REDEEM_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = RedeemRequest.class),
            examples = @ExampleObject(value = ShopControllerSwagger.REDEEM_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product redeemed; order created.", content = @Content(
                    schema = @Schema(implementation = RedeemResponse.class),
                    examples = @ExampleObject(value = ShopControllerSwagger.REDEEM_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ShopControllerSwagger.REDEEM_404_EXAMPLE))),
            @ApiResponse(responseCode = "403", description = "User's level is too low, or this item requires Premium.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ShopControllerSwagger.REDEEM_403_EXAMPLE))),
            @ApiResponse(responseCode = "402", description = "Not enough SpotPoints yet.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ShopControllerSwagger.REDEEM_402_EXAMPLE)))
    })
    @PostMapping("/redeem")
    public RedeemResponse redeem(@CurrentUserId UUID userId, @Valid @RequestBody RedeemRequest request) {
        return shopWriteService.redeem(userId, request.productId());
    }

    @Operation(summary = ShopControllerSwagger.ORDERS_SUMMARY, description = ShopControllerSwagger.ORDERS_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order history.", content = @Content(
                    schema = @Schema(implementation = OrderResponse.class),
                    examples = @ExampleObject(value = ShopControllerSwagger.ORDERS_200_EXAMPLE)))
    })
    @GetMapping("/orders")
    public List<OrderResponse> orders(@CurrentUserId UUID userId) {
        return shopReadService.listOrders(userId);
    }
}
