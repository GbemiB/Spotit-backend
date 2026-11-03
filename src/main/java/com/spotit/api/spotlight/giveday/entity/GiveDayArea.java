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
@Table(name = "giveday_areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayArea {
    @Id
    @Column(length = 30)
    private String id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 60)
    private String region;

    @Column(name = "map_x", nullable = false)
    private int mapX;

    @Column(name = "map_y", nullable = false)
    private int mapY;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;
}
