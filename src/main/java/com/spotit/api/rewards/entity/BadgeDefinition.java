package com.spotit.api.rewards.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Not a JPA entity — stored as a single JSON row in global_configuration (see
// com.spotit.api.rewards.repository.BadgeDefinitionRepository), not its own table.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeDefinition {
    private String id;
    private String name;
    private String description;
}
