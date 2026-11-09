package com.spotit.api.spotlight.games.padbank.entity;

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
@Table(name = "padbank_visitors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PadBankVisitor {
    public static final String PICKUP = "pickup";
    public static final String DELIVERY = "delivery";

    @Id
    @Column(length = 40)
    private String id;

    @Column(name = "day_number", nullable = false)
    private int day;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false, length = 60)
    private String who;

    @Column(name = "look", nullable = false, length = 20)
    private String look;

    @Column(nullable = false, columnDefinition = "text")
    private String clues;

    @Column(nullable = false, length = 200)
    private String packs;

    @Column(nullable = false, length = 10)
    private String route;

    @Column(nullable = false, length = 200)
    private String thanks;

    @Column(name = "route_why", nullable = false, length = 300)
    private String routeWhy;

    @Column(name = "group_why", length = 300)
    private String groupWhy;

    @Column(name = "wrong_why", nullable = false, columnDefinition = "text")
    private String wrongWhy;

    @Column(nullable = false)
    private boolean active;
}
