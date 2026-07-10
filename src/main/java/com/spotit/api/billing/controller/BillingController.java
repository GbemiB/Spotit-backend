package com.spotit.api.billing.controller;

import com.spotit.api.billing.dto.*;
import com.spotit.api.billing.service.BillingReadService;
import com.spotit.api.billing.service.BillingWriteService;
import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.security.CurrentUserId;
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

import java.util.UUID;

@Tag(name = "Billing", description = "Premium subscription status, purchase, cancellation, and restore.")
@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingReadService billingReadService;
    private final BillingWriteService billingWriteService;

    @Operation(summary = BillingControllerSwagger.STATUS_SUMMARY, description = BillingControllerSwagger.STATUS_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription status.", content = @Content(
                    schema = @Schema(implementation = SubscriptionResponse.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.STATUS_200_EXAMPLE)))
    })
    @GetMapping("/subscription")
    public SubscriptionResponse status(@CurrentUserId UUID userId) {
        return billingReadService.getStatus(userId);
    }

    @Operation(summary = BillingControllerSwagger.SUBSCRIBE_SUMMARY, description = BillingControllerSwagger.SUBSCRIBE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = SubscribeRequest.class),
            examples = @ExampleObject(value = BillingControllerSwagger.SUBSCRIBE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription activated.", content = @Content(
                    schema = @Schema(implementation = SubscriptionResponse.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.SUBSCRIBE_200_EXAMPLE))),
            @ApiResponse(responseCode = "402", description = "Receipt could not be verified.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.SUBSCRIBE_402_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "User already has an active subscription.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.SUBSCRIBE_409_EXAMPLE)))
    })
    @PostMapping("/subscription")
    public SubscriptionResponse subscribe(@CurrentUserId UUID userId, @Valid @RequestBody SubscribeRequest request) {
        return billingWriteService.subscribe(userId, request);
    }

    @Operation(summary = BillingControllerSwagger.CANCEL_SUMMARY, description = BillingControllerSwagger.CANCEL_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Auto-renew disabled.", content = @Content(
                    schema = @Schema(implementation = CancelResponse.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.CANCEL_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "No subscription found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.CANCEL_404_EXAMPLE)))
    })
    @PostMapping("/subscription/cancel")
    public CancelResponse cancel(@CurrentUserId UUID userId) {
        return billingWriteService.cancel(userId);
    }

    @Operation(summary = BillingControllerSwagger.RESTORE_SUMMARY, description = BillingControllerSwagger.RESTORE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = RestoreRequest.class),
            examples = @ExampleObject(value = BillingControllerSwagger.RESTORE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription restored.", content = @Content(
                    schema = @Schema(implementation = SubscriptionResponse.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.RESTORE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "No previous purchase found for this account.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BillingControllerSwagger.RESTORE_404_EXAMPLE)))
    })
    @PostMapping("/restore")
    public SubscriptionResponse restore(@CurrentUserId UUID userId, @Valid @RequestBody RestoreRequest request) {
        return billingWriteService.restore(userId, request);
    }
}
