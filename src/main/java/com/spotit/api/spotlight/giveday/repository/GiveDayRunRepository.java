package com.spotit.api.spotlight.giveday.repository;

import com.spotit.api.spotlight.giveday.entity.GiveDayRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GiveDayRunRepository extends JpaRepository<GiveDayRun, UUID> {
    List<GiveDayRun> findByUserId(UUID userId);

    long countByUserId(UUID userId);
}
