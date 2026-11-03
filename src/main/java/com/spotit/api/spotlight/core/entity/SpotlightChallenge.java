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
@Table(name = "spotlight_challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotlightChallenge {
    public static final String QUIZ = "quiz";
    public static final String GAME = "game";
    public static final String LIVE = "live";
    public static final String SOON = "soon";

    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, length = 10)
    private String kind;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(length = 200)
    private String blurb;

    @Column(length = 30)
    private String icon;

    @Column(nullable = false, length = 12)
    private String tone;

    @Column(nullable = false)
    private int cost;

    @Column(name = "cost_from", nullable = false)
    private boolean costFrom;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "free_plays", nullable = false, columnDefinition = "integer default 0")
    private int freePlays;

    @Column(nullable = false)
    private boolean active;
}
