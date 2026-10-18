package com.spotit.api.content.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentReference {
    @Column(nullable = false, length = 200)
    private String label;

    @Column(nullable = false, length = 500)
    private String url;
}
