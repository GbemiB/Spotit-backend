package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.dto.CreateLevelDefinitionRequest;
import com.spotit.api.rewards.dto.LevelDefinitionAdminResponse;
import com.spotit.api.rewards.dto.LevelDefinitionResponse;
import com.spotit.api.rewards.dto.UpdateLevelDefinitionRequest;
import com.spotit.api.rewards.entity.LevelDefinition;
import com.spotit.api.rewards.repository.LevelDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LevelDefinitionServiceImpl implements LevelDefinitionService {

    private final LevelDefinitionRepository levelDefinitionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LevelUtil.LevelDef> getLevelDefs() {
        return levelDefinitionRepository.findAllByOrderBySortOrderAsc().stream()
                .map(d -> new LevelUtil.LevelDef(d.getName(), d.getPointsLow(), d.getPointsHigh()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getLevelOrder() {
        List<String> names = levelDefinitionRepository.findAllByOrderBySortOrderAsc().stream()
                .map(LevelDefinition::getName)
                .toList();
        return Stream.concat(names.stream(), Stream.of(LevelUtil.MAX_LEVEL_NAME)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LevelDefinitionResponse> getLevels() {
        return levelDefinitionRepository.findAllByOrderBySortOrderAsc().stream()
                .map(d -> new LevelDefinitionResponse(d.getName(), d.getPointsLow(), d.getPointsHigh()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LevelDefinitionAdminResponse> listDefinitionsForAdmin() {
        return levelDefinitionRepository.findAllByOrderBySortOrderAsc().stream().map(this::toAdminResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LevelDefinitionAdminResponse getDefinitionForAdmin(String id) {
        return levelDefinitionRepository.findById(id)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.LEVEL_NOT_FOUND));
    }

    @Override
    @Transactional
    public LevelDefinitionAdminResponse createDefinition(CreateLevelDefinitionRequest request) {
        if (levelDefinitionRepository.existsById(request.id())) {
            throw new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, ErrorMessage.levelAlreadyExists(request.id()));
        }
        LevelDefinition def = LevelDefinition.builder()
                .id(request.id())
                .name(request.name())
                .pointsLow(request.pointsLow())
                .pointsHigh(request.pointsHigh())
                .sortOrder(request.sortOrder())
                .build();
        levelDefinitionRepository.save(def);
        return toAdminResponse(def);
    }

    @Override
    @Transactional
    public LevelDefinitionAdminResponse updateDefinition(String id, UpdateLevelDefinitionRequest request) {
        LevelDefinition def = levelDefinitionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.LEVEL_NOT_FOUND));
        if (request.name() != null) def.setName(request.name());
        if (request.pointsLow() != null) def.setPointsLow(request.pointsLow());
        if (request.pointsHigh() != null) def.setPointsHigh(request.pointsHigh());
        if (request.sortOrder() != null) def.setSortOrder(request.sortOrder());
        levelDefinitionRepository.save(def);
        return toAdminResponse(def);
    }

    @Override
    @Transactional
    public void deleteDefinition(String id) {
        if (!levelDefinitionRepository.existsById(id)) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.LEVEL_NOT_FOUND);
        }
        levelDefinitionRepository.deleteById(id);
    }

    private LevelDefinitionAdminResponse toAdminResponse(LevelDefinition def) {
        return new LevelDefinitionAdminResponse(def.getId(), def.getName(), def.getPointsLow(), def.getPointsHigh(), def.getSortOrder());
    }
}
