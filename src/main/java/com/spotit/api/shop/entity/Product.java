package com.spotit.api.shop.entity;

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
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private int cost;

    @Column(name = "min_level", nullable = false, length = 20)
    private String minLevel;

    @Column(name = "premium_only", nullable = false)
    private boolean premiumOnly;

    @Column(length = 8)
    private String icon;

    @Column(nullable = false)
    private boolean active;
}
