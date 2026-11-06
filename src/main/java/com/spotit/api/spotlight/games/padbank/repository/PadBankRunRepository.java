package com.spotit.api.spotlight.games.padbank.repository;

import com.spotit.api.spotlight.games.padbank.entity.PadBankRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PadBankRunRepository extends JpaRepository<PadBankRun, UUID> {
    @Query("SELECT r.day, MAX(r.stars) FROM PadBankRun r WHERE r.userId = :userId AND r.status = 'done' GROUP BY r.day")
    List<Object[]> bestStarsByDay(UUID userId);

    @Query("SELECT DISTINCT r.playedOn FROM PadBankRun r WHERE r.userId = :userId AND r.playedOn >= :since")
    List<LocalDate> findPlayDaysSince(UUID userId, LocalDate since);

    long countByUserId(UUID userId);
}
