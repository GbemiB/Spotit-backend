package com.spotit.api.partner.entity;

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
@Table(name = "love_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoveOption {
    public static final String PROMISE = "promise";
    public static final String EXPRESSION = "expression";
    public static final String STARTER = "starter";
    public static final String CATEGORY = "category";

    @Id
    @Column(length = 40)
    private String id;

    @Column(nullable = false, length = 12)
    private String kind;

    @Column(nullable = false, length = 80)
    private String label;

    @Column(length = 120)
    private String sub;

    @Column(length = 16)
    private String emoji;

    @Column(length = 60)
    private String phrase;

    @Column(name = "stored_value")
    private Integer storedValue;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;
}
