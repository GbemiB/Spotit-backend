package com.spotit.api.rewards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A SpotPoints level tier — was a hardcoded LEVELS array (mirrored in spotit-mobile's
 * levels.js and this module's LevelUtil), now admin-configurable like badges/challenges/
 * products. {@code sortOrder} defines progression order; {@code pointsHigh} is exclusive
 * (a user with exactly {@code pointsHigh} points has rolled into the next tier) — see
 * LevelUtil#levelFor.
 */
@Entity
@Table(name = "level_definitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelDefinition {

    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, length = 40, unique = true)
    private String name;

    @Column(name = "points_low", nullable = false)
    private long pointsLow;

    @Column(name = "points_high", nullable = false)
    private long pointsHigh;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
