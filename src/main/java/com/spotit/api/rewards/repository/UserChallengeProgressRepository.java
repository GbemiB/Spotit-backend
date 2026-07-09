package com.spotit.api.rewards.repository;

import com.spotit.api.rewards.entity.UserChallengeProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UserChallengeProgressRepository extends JpaRepository<UserChallengeProgress, UUID> {

    Optional<UserChallengeProgress> findByUserIdAndChallengeIdAndWeekStartDate(UUID userId, String challengeId, LocalDate weekStartDate);

    void deleteByUserId(UUID userId);
}
