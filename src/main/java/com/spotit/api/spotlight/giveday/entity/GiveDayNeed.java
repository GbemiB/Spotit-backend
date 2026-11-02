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
@Table(name = "giveday_needs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiveDayNeed {
    public static final String GOOD = "good";
    public static final String OK = "ok";
    public static final String POOR = "poor";

    @Id
    @Column(length = 70)
    private String id;

    @Column(name = "place_id", nullable = false, length = 30)
    private String placeId;

    @Column(name = "product_id", nullable = false, length = 30)
    private String productId;

    @Column(nullable = false, length = 10)
    private String rating;

    @Column(nullable = false, length = 300)
    private String why;
}
