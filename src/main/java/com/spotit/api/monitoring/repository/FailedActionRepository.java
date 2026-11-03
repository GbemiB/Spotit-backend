package com.spotit.api.monitoring.repository;

import com.spotit.api.monitoring.entity.FailedAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface FailedActionRepository extends JpaRepository<FailedAction, UUID> {
    List<FailedAction> findByHappenedAtBetween(Instant from, Instant to);
}
