package com.spotit.api.rewards.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.entity.GlobalConfiguration;
import com.spotit.api.configuration.repository.GlobalConfigurationRepository;
import com.spotit.api.rewards.entity.LevelDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Level definitions live in global_configuration, one JSON row per definition (name =
// "level-definition-{id}") rather than their own table — see LevelDefinition. This class keeps
// the same method surface JpaRepository gave callers (LevelDefinitionServiceImpl,
// ReferenceDataSeeder) so none of them needed to change.
@Repository
@RequiredArgsConstructor
public class LevelDefinitionRepository {

    private final GlobalConfigurationRepository repository;
    private final ObjectMapper objectMapper;

    public long count() {
        return repository.findByNameStartingWithOrderByNameAsc(PropertyNames.LEVEL_DEFINITION_PREFIX).size();
    }

    public List<LevelDefinition> findAll() {
        return repository.findByNameStartingWithOrderByNameAsc(PropertyNames.LEVEL_DEFINITION_PREFIX).stream()
                .map(this::toDefinition)
                .toList();
    }

    public List<LevelDefinition> findAllByOrderBySortOrderAsc() {
        return findAll().stream().sorted(Comparator.comparingInt(LevelDefinition::getSortOrder)).toList();
    }

    public Optional<LevelDefinition> findById(String id) {
        return repository.findByName(PropertyNames.levelDefinitionName(id)).map(this::toDefinition);
    }

    public boolean existsById(String id) {
        return repository.findByName(PropertyNames.levelDefinitionName(id)).isPresent();
    }

    public LevelDefinition save(LevelDefinition definition) {
        GlobalConfiguration row = repository.findByName(PropertyNames.levelDefinitionName(definition.getId()))
                .orElseGet(() -> GlobalConfiguration.builder()
                        .name(PropertyNames.levelDefinitionName(definition.getId()))
                        .groupName(PropertyNames.GROUP_LEVELS)
                        .enabled(true)
                        .build());
        row.setStringValue(toJson(definition));
        row.setDescription("Level definition: " + definition.getName());
        repository.save(row);
        return definition;
    }

    public List<LevelDefinition> saveAll(List<LevelDefinition> definitions) {
        return definitions.stream().map(this::save).toList();
    }

    public void deleteById(String id) {
        repository.findByName(PropertyNames.levelDefinitionName(id)).ifPresent(repository::delete);
    }

    private LevelDefinition toDefinition(GlobalConfiguration row) {
        try {
            return objectMapper.readValue(row.getStringValue(), LevelDefinition.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Corrupt level definition JSON in global_configuration row " + row.getName(), e);
        }
    }

    private String toJson(LevelDefinition definition) {
        try {
            return objectMapper.writeValueAsString(definition);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize level definition " + definition.getId(), e);
        }
    }
}
