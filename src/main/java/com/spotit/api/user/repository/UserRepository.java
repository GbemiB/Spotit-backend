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

    /**
     * Locks the user row for the duration of the transaction so concurrent
     * read-modify-write updates to points/streak/claim dates (daily claim,
     * ad watch, shop redemption, challenge payouts) can't race each other
     * into a double-credit or a negative balance.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(@Param("id") UUID id);
}
