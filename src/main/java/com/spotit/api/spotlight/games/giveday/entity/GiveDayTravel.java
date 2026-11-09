package com.spotit.api.spotlight.games.giveday.entity;

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
@Table(name = "giveday_travel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayTravel {
    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false, length = 30)
    private String label;

    @Column(nullable = false)
    private int cost;

    @Column(name = "units_per_minute", nullable = false)
    private int unitsPerMinute;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;
}
