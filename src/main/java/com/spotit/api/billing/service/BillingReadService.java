package com.spotit.api.billing.service;

import com.spotit.api.billing.dto.SubscriptionResponse;

import java.util.UUID;

public interface BillingReadService {
    SubscriptionResponse getStatus(UUID userId);
}
