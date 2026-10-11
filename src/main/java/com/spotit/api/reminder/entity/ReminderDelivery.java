package com.spotit.api.reminder.entity;

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

/**
 * A reminder that was sent. The unique key (user, type, local date) is what makes a duplicate send impossible,
 * however often the job runs; the rows also back the in-app notification inbox.
 */
@Entity
@Table(name = "reminder_deliveries",
        uniqueConstraints = @UniqueConstraint(name = "uq_reminder_deliveries_user_type_date", columnNames = {"user_id", "type", "for_date"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderDelivery {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReminderType type;

    // The user's local date the reminder was for.
    @Column(name = "for_date", nullable = false)
    private LocalDate forDate;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false, length = 300)
    private String body;

    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    @Column(name = "is_read", nullable = false)
    private boolean read;
}
