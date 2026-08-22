package com.spotit.api.log.repository;

import com.spotit.api.log.entity.CycleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CycleLogRepository extends JpaRepository<CycleLog, UUID> {

    Optional<CycleLog> findByUserIdAndLogDate(UUID userId, LocalDate logDate);

    List<CycleLog> findByUserIdAndLogDateBetweenOrderByLogDateAsc(UUID userId, LocalDate from, LocalDate to);

    List<CycleLog> findByUserIdAndFlowIsNotNull(UUID userId);

    long countByUserId(UUID userId);

    void deleteByUserIdAndLogDate(UUID userId, LocalDate logDate);

    void deleteByUserId(UUID userId);
}
