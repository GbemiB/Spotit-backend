package com.spotit.api.content.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "image_key", length = 60)
    private String imageKey;

    @Column(nullable = false)
    private boolean sponsored;

    @Column(length = 120)
    private String advertiser;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "content_item_references", joinColumns = @JoinColumn(name = "content_item_id"))
    @OrderColumn(name = "position")
    private List<ContentReference> references = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}
