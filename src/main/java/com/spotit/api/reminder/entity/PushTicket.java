package com.spotit.api.reminder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * Expo accepts a push and returns a ticket; whether Google or Apple delivered it is only known from the receipt,
 * a while later. Tickets wait here until the job collects their receipts.
 */
@Entity
@Table(name = "push_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushTicket {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "ticket_id", nullable = false, unique = true, length = 100)
    private String ticketId;

    @Column(name = "push_token", nullable = false, length = 400)
    private String pushToken;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
