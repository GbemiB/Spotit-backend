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
