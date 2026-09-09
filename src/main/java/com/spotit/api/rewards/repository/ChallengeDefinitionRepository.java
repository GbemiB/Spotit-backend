package com.spotit.api.rewards.repository;

import com.spotit.api.rewards.entity.ChallengeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeDefinitionRepository extends JpaRepository<ChallengeDefinition, String> {
}
