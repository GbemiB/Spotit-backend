package com.spotit.api.billing.controller;

import com.spotit.api.billing.dto.*;
import com.spotit.api.billing.service.BillingReadService;
import com.spotit.api.billing.service.BillingWriteService;
import com.spotit.api.common.security.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingReadService billingReadService;
    private final BillingWriteService billingWriteService;

    @GetMapping("/subscription")
    public SubscriptionResponse status(@CurrentUserId UUID userId) {
        return billingReadService.getStatus(userId);
    }

    @PostMapping("/subscription")
    public SubscriptionResponse subscribe(@CurrentUserId UUID userId, @Valid @RequestBody SubscribeRequest request) {
        return billingWriteService.subscribe(userId, request);
    }

    @PostMapping("/subscription/cancel")
    public CancelResponse cancel(@CurrentUserId UUID userId) {
        return billingWriteService.cancel(userId);
    }

    @PostMapping("/restore")
    public SubscriptionResponse restore(@CurrentUserId UUID userId, @Valid @RequestBody RestoreRequest request) {
        return billingWriteService.restore(userId, request);
    }
}
