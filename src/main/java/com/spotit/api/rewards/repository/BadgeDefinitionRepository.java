package com.spotit.api.rewards.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.entity.GlobalConfiguration;
import com.spotit.api.configuration.repository.GlobalConfigurationRepository;
import com.spotit.api.rewards.entity.BadgeDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Badge definitions live in global_configuration, one JSON row per definition (name =
// "badge-definition-{id}") rather than their own table — see BadgeDefinition. This class keeps
// the same method surface JpaRepository gave callers (BadgeReadServiceImpl,
// BadgeWriteServiceImpl, ReferenceDataSeeder) so none of them needed to change.
@Repository
@RequiredArgsConstructor
public class BadgeDefinitionRepository {

    private final GlobalConfigurationRepository repository;
    private final ObjectMapper objectMapper;

    public long count() {
        return repository.findByNameStartingWithOrderByNameAsc(PropertyNames.BADGE_DEFINITION_PREFIX).size();
    }

    public List<BadgeDefinition> findAll() {
        return repository.findByNameStartingWithOrderByNameAsc(PropertyNames.BADGE_DEFINITION_PREFIX).stream()
                .map(this::toDefinition)
                .toList();
    }

    public Optional<BadgeDefinition> findById(String id) {
        return repository.findByName(PropertyNames.badgeDefinitionName(id)).map(this::toDefinition);
    }

    public boolean existsById(String id) {
        return repository.findByName(PropertyNames.badgeDefinitionName(id)).isPresent();
    }

    public BadgeDefinition save(BadgeDefinition definition) {
        GlobalConfiguration row = repository.findByName(PropertyNames.badgeDefinitionName(definition.getId()))
                .orElseGet(() -> GlobalConfiguration.builder()
                        .name(PropertyNames.badgeDefinitionName(definition.getId()))
                        .groupName(PropertyNames.GROUP_BADGES)
                        .enabled(true)
                        .build());
        row.setStringValue(toJson(definition));
        row.setDescription("Badge definition: " + definition.getName());
        repository.save(row);
        return definition;
    }

    public List<BadgeDefinition> saveAll(List<BadgeDefinition> definitions) {
        return definitions.stream().map(this::save).toList();
    }

    public void deleteById(String id) {
        repository.findByName(PropertyNames.badgeDefinitionName(id)).ifPresent(repository::delete);
    }

    private BadgeDefinition toDefinition(GlobalConfiguration row) {
        try {
            return objectMapper.readValue(row.getStringValue(), BadgeDefinition.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Corrupt badge definition JSON in global_configuration row " + row.getName(), e);
        }
    }

    private String toJson(BadgeDefinition definition) {
        try {
            return objectMapper.writeValueAsString(definition);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize badge definition " + definition.getId(), e);
        }
    }
}
