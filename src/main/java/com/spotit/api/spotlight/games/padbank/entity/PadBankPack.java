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
@Table(name = "padbank_packs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PadBankPack {
    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, length = 12)
    private String color;

    @Column(length = 400)
    private String contents;

    @Column(name = "from_day", nullable = false, columnDefinition = "integer default 1")
    private int fromDay;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;
}
