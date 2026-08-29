package com.spotit.api.rewards.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Not a JPA entity — stored as a single JSON row in global_configuration (see
// com.spotit.api.rewards.repository.ChallengeDefinitionRepository), not its own table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeDefinition {
    private String id;
    private String title;
    private int reward;
    private int total;
    private ChallengeType type;
}
