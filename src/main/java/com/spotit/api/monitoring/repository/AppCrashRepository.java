package com.spotit.api.monitoring.repository;

import com.spotit.api.monitoring.entity.AppCrash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AppCrashRepository extends JpaRepository<AppCrash, UUID> {
    List<AppCrash> findByHappenedAtBetween(Instant from, Instant to);

    Page<AppCrash> findByHappenedAtBetweenOrderByHappenedAtDesc(Instant from, Instant to, Pageable pageable);
}
