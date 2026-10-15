package com.spotit.api.reminder.repository;

import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReminderDeliveryRepository extends JpaRepository<ReminderDelivery, UUID> {
    boolean existsByUserIdAndTypeAndForDate(UUID userId, ReminderType type, LocalDate forDate);

    List<ReminderDelivery> findTop50ByUserIdOrderBySentAtDesc(UUID userId);

    long countByUserIdAndReadFalse(UUID userId);

    @Modifying
    @Query("update ReminderDelivery d set d.read = true where d.userId = :userId and d.read = false")
    int markAllRead(@Param("userId") UUID userId);

    void deleteByUserId(UUID userId);

    void deleteBySentAtBefore(Instant instant);
}
