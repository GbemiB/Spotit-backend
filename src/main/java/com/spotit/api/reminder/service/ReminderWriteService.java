package com.spotit.api.reminder.service;

import com.spotit.api.reminder.entity.PushTicket;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.push.PushResult;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReminderWriteService {
    /** Records a reminder as sent. Empty when this user already has that reminder for that date, so the caller must not send. */
    Optional<ReminderDelivery> record(UUID userId, ReminderType type, LocalDate forDate, String body, Instant now);

    void markAllRead(UUID userId);

    /** Keeps the tickets of accepted pushes for the receipt check, and forgets devices the push service no longer knows. */
    void applySendResults(List<PushResult> results, Instant now);

    /** Tickets old enough for their receipt to be ready. */
    List<PushTicket> ticketsDue(Instant now);

    /** Applies receipts (ticket id to error code, empty when delivered): drops dead devices and the tickets now settled. */
    void applyReceipts(List<PushTicket> tickets, Map<String, String> receipts, Instant now);

    void purgeOld(Instant now);
}
