package com.spotit.api.reminder;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.reminder.entity.PushTicket;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.service.ReminderPlanner;
import com.spotit.api.reminder.service.ReminderWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * One pass of the reminder job: work out who is due what in her own local time, record it, push it.
 *
 * <p>The delivery record is written before the push. If the server stops between the two, that reminder is lost
 * rather than sent twice on restart, which is the better failure for a notification.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderJob {
    private final ConfigurationDomainService configurationDomainService;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final ReminderPlanner reminderPlanner;
    private final ReminderWriteService reminderWriteService;
    private final PushSender pushSender;

    public void run(Instant now) {
        ReminderSettings settings = configurationDomainService.getReminderSettings();
        if (!settings.enabled()) {
            return;
        }
        collectReceipts(now);

        List<User> users = userRepository.findReminderCandidates();
        if (users.isEmpty()) {
            return;
        }
        Map<UUID, List<Device>> devicesByUser = deviceRepository.findByUserIdIn(users.stream().map(User::getId).toList()).stream()
                .filter(device -> isExpoToken(device.getPushToken()))
                .collect(Collectors.groupingBy(Device::getUserId));

        List<PushMessage> messages = new ArrayList<>();
        for (User user : users) {
            List<Device> devices = devicesByUser.get(user.getId());
            if (devices == null) {
                continue;
            }
            try {
                messages.addAll(remindersFor(user, devices, settings, now));
            } catch (RuntimeException e) {
                // One user's bad data must not cost everyone else their reminder.
                log.error("Reminder planning failed for user {}", user.getId(), e);
            }
        }
        if (!messages.isEmpty()) {
            reminderWriteService.applySendResults(pushSender.send(messages), now);
            log.info("Reminder job sent {} push message(s)", messages.size());
        }
    }

    private List<PushMessage> remindersFor(User user, List<Device> devices, ReminderSettings settings, Instant now) {
        LocalDateTime localNow = LocalDateTime.ofInstant(now, zoneOf(user, devices));
        List<PushMessage> messages = new ArrayList<>();
        for (ReminderPlanner.Planned planned : reminderPlanner.plan(user, settings, localNow)) {
            Optional<ReminderDelivery> delivery;
            try {
                delivery = reminderWriteService.record(user.getId(), planned.type(), localNow.toLocalDate(), planned.body(), now);
            } catch (DataIntegrityViolationException e) {
                // Another run recorded it in the same instant.
                continue;
            }
            delivery.ifPresent(sent -> devices.forEach(device -> messages.add(new PushMessage(
                    device.getPushToken(), sent.getTitle(), sent.getBody(),
                    Map.of("type", sent.getType().name(), "screen", sent.getType().getScreen(), "id", sent.getId().toString())))));
        }
        return messages;
    }

    private void collectReceipts(Instant now) {
        List<PushTicket> tickets = reminderWriteService.ticketsDue(now);
        if (tickets.isEmpty()) {
            return;
        }
        Map<String, String> receipts = pushSender.receipts(tickets.stream().map(PushTicket::getTicketId).toList());
        reminderWriteService.applyReceipts(tickets, receipts, now);
    }

    // The account's zone follows the phone opened last; a device's own zone covers accounts registered before that existed.
    private static ZoneId zoneOf(User user, List<Device> devices) {
        return Optional.ofNullable(user.getTimeZone())
                .or(() -> devices.stream().map(Device::getTimeZone).filter(Objects::nonNull).findFirst())
                .map(ReminderJob::parseZone)
                .orElse(ZoneOffset.UTC);
    }

    private static ZoneId parseZone(String zone) {
        try {
            return ZoneId.of(zone);
        } catch (DateTimeException e) {
            return ZoneOffset.UTC;
        }
    }

    // Version 1.0.0 registered a device id here, not a push address; those rows cannot receive anything.
    static boolean isExpoToken(String pushToken) {
        return pushToken != null && (pushToken.startsWith("ExponentPushToken[") || pushToken.startsWith("ExpoPushToken["));
    }
}
