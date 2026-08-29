package com.spotit.api.content.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "content_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentItem {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 40)
    private String tag;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(columnDefinition = "text")
    private String body;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // Presentation-only: the client maps this to a locally bundled image rather than fetching
    // imageUrl remotely (mirrors how badge/product icons are mapped locally by id elsewhere).
    @Column(name = "image_key", length = 60)
    private String imageKey;

    @Column(nullable = false)
    private boolean sponsored;

    @Column(length = 120)
    private String advertiser;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}
