package com.spotit.api.rewards.service;

import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.dto.CreateLevelDefinitionRequest;
import com.spotit.api.rewards.dto.LevelDefinitionAdminResponse;
import com.spotit.api.rewards.dto.LevelDefinitionResponse;
import com.spotit.api.rewards.dto.UpdateLevelDefinitionRequest;

import java.util.List;

public interface LevelDefinitionService {

    // -- read, for LevelUtil callers (RewardsReadService, ShopReadService/WriteService) --------

    /** Ascending sortOrder — the shape LevelUtil.levelFor/meetsMinLevel need. */
    List<LevelUtil.LevelDef> getLevelDefs();

    /** Tier names in ascending order, plus LevelUtil.MAX_LEVEL_NAME at the end. */
    List<String> getLevelOrder();

    // -- client-facing read --------

    List<LevelDefinitionResponse> getLevels();

    // -- global configuration: admin CRUD --------

    List<LevelDefinitionAdminResponse> listDefinitionsForAdmin();

    LevelDefinitionAdminResponse getDefinitionForAdmin(String id);

    LevelDefinitionAdminResponse createDefinition(CreateLevelDefinitionRequest request);

    LevelDefinitionAdminResponse updateDefinition(String id, UpdateLevelDefinitionRequest request);

    void deleteDefinition(String id);
}
