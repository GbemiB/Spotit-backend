package com.spotit.api.rewards.service;

import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.dto.CreateLevelDefinitionRequest;
import com.spotit.api.rewards.dto.LevelDefinitionAdminResponse;
import com.spotit.api.rewards.dto.LevelDefinitionResponse;
import com.spotit.api.rewards.dto.UpdateLevelDefinitionRequest;

import java.util.List;

public interface LevelDefinitionService {
    List<LevelUtil.LevelDef> getLevelDefs();

    List<String> getLevelOrder();

    List<LevelDefinitionResponse> getLevels();

    List<LevelDefinitionAdminResponse> listDefinitionsForAdmin();

    LevelDefinitionAdminResponse getDefinitionForAdmin(String id);

    LevelDefinitionAdminResponse createDefinition(CreateLevelDefinitionRequest request);

    LevelDefinitionAdminResponse updateDefinition(String id, UpdateLevelDefinitionRequest request);

    void deleteDefinition(String id);
}
