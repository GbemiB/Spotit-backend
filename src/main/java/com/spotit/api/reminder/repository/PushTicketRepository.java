package com.spotit.api.reminder.repository;

import com.spotit.api.reminder.entity.PushTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PushTicketRepository extends JpaRepository<PushTicket, UUID> {
    List<PushTicket> findTop1000ByCreatedAtBeforeOrderByCreatedAtAsc(Instant instant);
}
