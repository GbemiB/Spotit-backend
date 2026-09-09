package com.spotit.api.rewards.repository;

import com.spotit.api.rewards.entity.BadgeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeDefinitionRepository extends JpaRepository<BadgeDefinition, String> {
}
