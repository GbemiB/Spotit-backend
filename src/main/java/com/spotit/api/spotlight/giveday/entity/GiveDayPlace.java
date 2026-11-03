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
@Table(name = "giveday_places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayPlace {
    @Id
    @Column(length = 40)
    private String id;

    @Column(name = "area_id", nullable = false, length = 30)
    private String areaId;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 60)
    private String kind;

    @Column(nullable = false, length = 300)
    private String teaser;

    @Column(length = 300)
    private String quote;

    @Column(name = "host_name", nullable = false, length = 60)
    private String hostName;

    @Column(name = "host_look", nullable = false, length = 20)
    private String hostLook;

    @Column(nullable = false, length = 40)
    private String who;

    @Column(name = "people_total", nullable = false)
    private int total;

    @Column(length = 300)
    private String thanks;

    @Column(nullable = false, columnDefinition = "text")
    private String facts;

    @Column(name = "map_x", nullable = false)
    private int mapX;

    @Column(name = "map_y", nullable = false)
    private int mapY;

    @Column(name = "for_self", nullable = false)
    private boolean forSelf;

    @Column(name = "closes_at", nullable = false)
    private int closes;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;
}
