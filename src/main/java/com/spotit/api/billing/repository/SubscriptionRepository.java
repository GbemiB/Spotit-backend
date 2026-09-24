package com.spotit.api.billing.repository;

import com.spotit.api.billing.entity.Subscription;
import com.spotit.api.billing.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByUserId(UUID userId);

    List<Subscription> findByStatusAndAutoRenewFalseAndRenewsAtBefore(SubscriptionStatus status, Instant instant);

    void deleteByUserId(UUID userId);
}
