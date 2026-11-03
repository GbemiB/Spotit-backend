package com.spotit.api.partner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "partner_check_ins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerCheckIn {
    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "partner_id", nullable = false)
    private UUID partnerId;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false, length = 60)
    private String supports;

    @Column(length = 200)
    private String note;

    @Column(length = 12)
    private String kind;

    @Column(length = 20)
    private String expression;

    @Column(name = "reply_heart")
    private Boolean replyHeart;

    @Column(name = "reply_note", length = 120)
    private String replyNote;

    @Column(name = "replied_at")
    private Instant repliedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}
