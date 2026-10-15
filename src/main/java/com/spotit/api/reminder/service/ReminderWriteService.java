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
    Optional<ReminderDelivery> record(UUID userId, ReminderType type, LocalDate forDate, String body, Instant now);

    void markAllRead(UUID userId);

    void applySendResults(List<PushResult> results, Instant now);

    List<PushTicket> ticketsDue(Instant now);

    void applyReceipts(List<PushTicket> tickets, Map<String, String> receipts, Instant now);

    void purgeOld(Instant now);
}
