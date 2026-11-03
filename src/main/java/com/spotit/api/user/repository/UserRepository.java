package com.spotit.api.user.repository;

import com.spotit.api.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
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

    List<User> findByTrackingStartDateIsNullAndLastPeriodDateIsNotNull();

    @Query("""
            select u from User u
            where u.pendingDeletionAt is null and u.onboarded = true
              and u.accountType = com.spotit.api.user.entity.AccountOwner.owner
              and (u.notifPeriod = true or u.notifOvulation = true or u.notifDailyLog = true or u.notifDigest = true)
              and u.id in (select d.userId from Device d)
            """)
    List<User> findReminderCandidates();

    @Modifying
    @Query("update User u set u.lastActiveAt = :now where u.id = :id")
    int touchLastActive(@Param("id") UUID id, @Param("now") Instant now);

    @Query("""
            select u from User u
            where u.onboarded = true
              and ((u.onboardedAt between :from and :to) or (u.onboardedAt is null and u.createdAt between :from and :to))
            """)
    List<User> findFirstOnboardedBetween(@Param("from") Instant from, @Param("to") Instant to);

    long countByCreatedAtBetween(Instant from, Instant to);

    long countByLastActiveAtAfter(Instant since);

    @Query("select u from User u order by case when u.lastActiveAt is null then 1 else 0 end, u.lastActiveAt desc")
    org.springframework.data.domain.Page<User> findByLastActive(org.springframework.data.domain.Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(@Param("id") UUID id);
}
