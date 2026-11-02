package com.spotit.api.spotlight.core.entity;

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
@Table(name = "spotlight_ranks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotlightRank {
    @Id
    @Column(length = 30)
    private String id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(name = "min_stars", nullable = false)
    private int minStars;

    @Column(nullable = false)
    private boolean active;
}
