package com.spotit.api.billing.service;

import com.spotit.api.billing.dto.CancelResponse;
import com.spotit.api.billing.dto.RestoreRequest;
import com.spotit.api.billing.dto.SubscribeRequest;
import com.spotit.api.billing.dto.SubscriptionResponse;

import java.util.UUID;

/**
 * No real App Store / Play Billing receipt verification is wired up — receipts
 * are only checked for non-blank presence. Swap {@link BillingWriteServiceImpl#subscribe}
 * to call a real verification service before production use.
 */
public interface BillingWriteService {

    SubscriptionResponse subscribe(UUID userId, SubscribeRequest request);

    CancelResponse cancel(UUID userId);

    SubscriptionResponse restore(UUID userId, RestoreRequest request);

    /** Runs hourly: expires subscriptions that were cancelled and have passed their renewal date. */
    void expireLapsedSubscriptions();
}
