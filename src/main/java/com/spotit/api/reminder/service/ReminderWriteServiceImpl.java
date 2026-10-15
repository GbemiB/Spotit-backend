package com.spotit.api.reminder.service;

import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.reminder.entity.PushTicket;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.push.PushResult;
import com.spotit.api.reminder.repository.PushTicketRepository;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReminderWriteServiceImpl implements ReminderWriteService {
    static final Duration RECEIPT_DELAY = Duration.ofMinutes(15);
    static final Duration TICKET_MAX_AGE = Duration.ofHours(24);
    static final Duration INBOX_RETENTION = Duration.ofDays(90);

    private final ReminderDeliveryRepository reminderDeliveryRepository;
    private final PushTicketRepository pushTicketRepository;
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public Optional<ReminderDelivery> record(UUID userId, ReminderType type, LocalDate forDate, String body, Instant now) {
        if (reminderDeliveryRepository.existsByUserIdAndTypeAndForDate(userId, type, forDate)) {
            return Optional.empty();
        }
        return Optional.of(reminderDeliveryRepository.saveAndFlush(ReminderDelivery.builder()
                .userId(userId).type(type).forDate(forDate).title(type.getTitle()).body(body).sentAt(now).build()));
    }

    @Override
    @Transactional
    public void markAllRead(UUID userId) {
        reminderDeliveryRepository.markAllRead(userId);
    }

    @Override
    @Transactional
    public void applySendResults(List<PushResult> results, Instant now) {
        List<PushTicket> tickets = results.stream()
                .filter(result -> result.ticketId() != null)
                .map(result -> PushTicket.builder().ticketId(result.ticketId()).pushToken(result.to()).createdAt(now).build())
                .toList();
        pushTicketRepository.saveAll(tickets);
        forgetDevices(results.stream().filter(PushResult::deviceGone).map(PushResult::to).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PushTicket> ticketsDue(Instant now) {
        return pushTicketRepository.findTop1000ByCreatedAtBeforeOrderByCreatedAtAsc(now.minus(RECEIPT_DELAY));
    }

    @Override
    @Transactional
    public void applyReceipts(List<PushTicket> tickets, Map<String, String> receipts, Instant now) {
        forgetDevices(tickets.stream()
                .filter(ticket -> PushResult.DEVICE_NOT_REGISTERED.equals(receipts.get(ticket.getTicketId())))
                .map(PushTicket::getPushToken)
                .toList());
        tickets.stream()
                .filter(ticket -> receipts.containsKey(ticket.getTicketId()))
                .filter(ticket -> !receipts.get(ticket.getTicketId()).isEmpty())
                .forEach(ticket -> log.warn("Push ticket {} was not delivered: {}", ticket.getTicketId(), receipts.get(ticket.getTicketId())));
        Instant expired = now.minus(TICKET_MAX_AGE);
        pushTicketRepository.deleteAll(tickets.stream()
                .filter(ticket -> receipts.containsKey(ticket.getTicketId()) || ticket.getCreatedAt().isBefore(expired))
                .toList());
    }

    @Override
    @Transactional
    public void purgeOld(Instant now) {
        reminderDeliveryRepository.deleteBySentAtBefore(now.minus(INBOX_RETENTION));
    }

    private void forgetDevices(List<String> pushTokens) {
        if (!pushTokens.isEmpty()) {
            deviceRepository.deleteByPushTokenIn(pushTokens);
            log.info("Removed {} device(s) the push service no longer recognises", pushTokens.size());
        }
    }
}
