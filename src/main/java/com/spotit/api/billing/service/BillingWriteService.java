package com.spotit.api.billing.service;

import com.spotit.api.billing.dto.CancelResponse;
import com.spotit.api.billing.dto.RestoreRequest;
import com.spotit.api.billing.dto.SubscribeRequest;
import com.spotit.api.billing.dto.SubscriptionResponse;

import java.util.UUID;

public interface BillingWriteService {
    SubscriptionResponse subscribe(UUID userId, SubscribeRequest request);

    CancelResponse cancel(UUID userId);

    SubscriptionResponse restore(UUID userId, RestoreRequest request);

    void expireLapsedSubscriptions();
}
