package com.spotit.api.rewards.repository;

import com.spotit.api.rewards.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserBadgeRepository extends JpaRepository<UserBadge, UserBadge.Key> {
    List<UserBadge> findByUserId(UUID userId);

    boolean existsByUserIdAndBadgeId(UUID userId, String badgeId);

    void deleteByUserId(UUID userId);
}
