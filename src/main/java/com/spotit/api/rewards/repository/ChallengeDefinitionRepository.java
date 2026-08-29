package com.spotit.api.rewards.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.api.configuration.PropertyNames;
import com.spotit.api.configuration.entity.GlobalConfiguration;
import com.spotit.api.configuration.repository.GlobalConfigurationRepository;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Challenge definitions live in global_configuration, one JSON row per definition (name =
// "challenge-definition-{id}") rather than their own table — see ChallengeDefinition. This class
// keeps the same method surface JpaRepository gave callers (ChallengeReadServiceImpl,
// ChallengeWriteServiceImpl, ReferenceDataSeeder) so none of them needed to change.
@Repository
@RequiredArgsConstructor
public class ChallengeDefinitionRepository {

    private final GlobalConfigurationRepository repository;
    private final ObjectMapper objectMapper;

    public long count() {
        return repository.findByNameStartingWithOrderByNameAsc(PropertyNames.CHALLENGE_DEFINITION_PREFIX).size();
    }

    public List<ChallengeDefinition> findAll() {
        return repository.findByNameStartingWithOrderByNameAsc(PropertyNames.CHALLENGE_DEFINITION_PREFIX).stream()
                .map(this::toDefinition)
                .toList();
    }

    public Optional<ChallengeDefinition> findById(String id) {
        return repository.findByName(PropertyNames.challengeDefinitionName(id)).map(this::toDefinition);
    }

    public boolean existsById(String id) {
        return repository.findByName(PropertyNames.challengeDefinitionName(id)).isPresent();
    }

    public ChallengeDefinition save(ChallengeDefinition definition) {
        GlobalConfiguration row = repository.findByName(PropertyNames.challengeDefinitionName(definition.getId()))
                .orElseGet(() -> GlobalConfiguration.builder()
                        .name(PropertyNames.challengeDefinitionName(definition.getId()))
                        .groupName(PropertyNames.GROUP_CHALLENGES)
                        .enabled(true)
                        .build());
        row.setStringValue(toJson(definition));
        row.setDescription("Challenge definition: " + definition.getTitle());
        repository.save(row);
        return definition;
    }

    public List<ChallengeDefinition> saveAll(List<ChallengeDefinition> definitions) {
        return definitions.stream().map(this::save).toList();
    }

    public void deleteById(String id) {
        repository.findByName(PropertyNames.challengeDefinitionName(id)).ifPresent(repository::delete);
    }

    private ChallengeDefinition toDefinition(GlobalConfiguration row) {
        try {
            return objectMapper.readValue(row.getStringValue(), ChallengeDefinition.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Corrupt challenge definition JSON in global_configuration row " + row.getName(), e);
        }
    }

    private String toJson(ChallengeDefinition definition) {
        try {
            return objectMapper.writeValueAsString(definition);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize challenge definition " + definition.getId(), e);
        }
    }
}
