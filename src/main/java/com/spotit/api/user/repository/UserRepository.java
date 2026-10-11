package com.spotit.api.user.repository;

import com.spotit.api.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<User> findByPendingDeletionAtBefore(Instant instant);

    // Everyone the reminder job has to look at: at least one reminder on and at least one registered device.
    @Query("""
            select u from User u
            where u.pendingDeletionAt is null and u.onboarded = true
              and (u.notifPeriod = true or u.notifOvulation = true or u.notifDailyLog = true or u.notifDigest = true)
              and u.id in (select d.userId from Device d)
            """)
    List<User> findReminderCandidates();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(@Param("id") UUID id);
}
