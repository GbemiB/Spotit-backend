package com.spotit.api.spotlight.giveday.entity;

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
@Table(name = "giveday_levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayLevel {
    @Id
    @Column(name = "level_number")
    private int number;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(name = "radius_km", nullable = false)
    private int radiusKm;

    @Column(name = "stars_needed", nullable = false)
    private int starsNeeded;

    @Column(name = "pass_cost", nullable = false)
    private int passCost;

    @Column(name = "gift_budget", nullable = false)
    private int giftBudget;

    @Column(nullable = false)
    private boolean active;
}
