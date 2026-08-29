package com.spotit.api.rewards.repository;

import com.spotit.api.rewards.entity.LevelDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LevelDefinitionRepository extends JpaRepository<LevelDefinition, String> {

    List<LevelDefinition> findAllByOrderBySortOrderAsc();
}
