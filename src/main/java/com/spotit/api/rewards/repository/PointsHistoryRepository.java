package com.spotit.api.rewards.repository;

import com.spotit.api.rewards.entity.PointsHistoryEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PointsHistoryRepository extends JpaRepository<PointsHistoryEntry, UUID> {
    List<PointsHistoryEntry> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    long countByUserIdAndOccurredOnAndLabel(UUID userId, LocalDate occurredOn, String label);

    void deleteByUserId(UUID userId);
}
