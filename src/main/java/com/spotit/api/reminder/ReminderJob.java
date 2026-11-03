package com.spotit.api.reminder;

import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.reminder.entity.PushTicket;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.service.PartnerReminderPlanner;
import com.spotit.api.reminder.service.ReminderPlanner;
import com.spotit.api.reminder.service.ReminderWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderJob {
    private final ReminderSettings settings;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final ReminderPlanner reminderPlanner;
    private final ReminderWriteService reminderWriteService;
    private final PushSender pushSender;
    private final PartnerLinkRepository partnerLinkRepository;
    private final PartnerReminderPlanner partnerReminderPlanner;

    public void run(Instant now) {
        if (!settings.enabled()) {
            return;
        }
        collectReceipts(now);

        List<PushMessage> messages = new ArrayList<>();
        messages.addAll(ownerReminders(now));
        messages.addAll(partnerReminders(now));
        if (!messages.isEmpty()) {
            reminderWriteService.applySendResults(pushSender.send(messages), now);
            log.info("Reminder job sent {} push message(s)", messages.size());
        }
    }

    private List<PushMessage> ownerReminders(Instant now) {
        List<User> users = userRepository.findReminderCandidates();
        if (users.isEmpty()) {
            return List.of();
        }
        Map<UUID, List<Device>> devicesByUser = devicesOf(users.stream().map(User::getId).toList());

        List<PushMessage> messages = new ArrayList<>();
        for (User user : users) {
            List<Device> devices = devicesByUser.get(user.getId());
            if (devices == null) {
                continue;
            }
            try {
                messages.addAll(remindersFor(user, devices, settings, now));
            } catch (RuntimeException e) {
                log.error("Reminder planning failed for user {}", user.getId(), e);
            }
        }
        return messages;
    }

    private List<PushMessage> partnerReminders(Instant now) {
        List<PartnerLink> links = partnerLinkRepository.findByStatusAndPartnerReminderTrue(PartnerLinkStatus.active);
        if (links.isEmpty()) {
            return List.of();
        }
        Set<UUID> userIds = new HashSet<>();
        links.forEach(link -> {
            userIds.add(link.getOwnerId());
            userIds.add(link.getPartnerId());
        });
        Map<UUID, User> usersById = userRepository.findAllById(userIds).stream()
                .filter(user -> user.getPendingDeletionAt() == null)
                .collect(Collectors.toMap(User::getId, Function.identity()));
        Map<UUID, List<Device>> devicesByUser = devicesOf(links.stream().map(PartnerLink::getPartnerId).toList());

        List<PushMessage> messages = new ArrayList<>();
        for (PartnerLink link : links) {
            User partner = usersById.get(link.getPartnerId());
            User owner = usersById.get(link.getOwnerId());
            List<Device> devices = devicesByUser.get(link.getPartnerId());
            if (partner == null || owner == null || devices == null) {
                continue;
            }
            try {
                messages.addAll(partnerReminderFor(link, partner, owner, devices, now));
            } catch (RuntimeException e) {
                log.error("Partner reminder planning failed for partner {}", partner.getId(), e);
            }
        }
        return messages;
    }

    private List<PushMessage> partnerReminderFor(PartnerLink link, User partner, User owner, List<Device> devices, Instant now) {
        LocalDateTime localNow = LocalDateTime.ofInstant(now, UserClock.zoneOf(partner, devices));
        Optional<PartnerReminderPlanner.Planned> planned = partnerReminderPlanner.plan(link, owner, settings, localNow);
        if (planned.isEmpty()) {
            return List.of();
        }
        Optional<ReminderDelivery> delivery;
        try {
            delivery = reminderWriteService.record(partner.getId(), planned.get().type(), localNow.toLocalDate(), planned.get().body(), now);
        } catch (DataIntegrityViolationException e) {
            return List.of();
        }
        link.setLastRemindedPeriodDate(planned.get().periodDate());
        partnerLinkRepository.save(link);
        return delivery.map(sent -> toMessages(sent, devices)).orElse(List.of());
    }

    private Map<UUID, List<Device>> devicesOf(List<UUID> userIds) {
        return deviceRepository.findByUserIdIn(userIds).stream()
                .filter(device -> isExpoToken(device.getPushToken()))
                .collect(Collectors.groupingBy(Device::getUserId));
    }

    private static List<PushMessage> toMessages(ReminderDelivery sent, List<Device> devices) {
        return devices.stream().map(device -> new PushMessage(
                device.getPushToken(), sent.getTitle(), sent.getBody(),
                Map.of("type", sent.getType().name(), "screen", sent.getType().getScreen(), "id", sent.getId().toString()))).toList();
    }

    private List<PushMessage> remindersFor(User user, List<Device> devices, ReminderSettings settings, Instant now) {
        LocalDateTime localNow = LocalDateTime.ofInstant(now, UserClock.zoneOf(user, devices));
        List<PushMessage> messages = new ArrayList<>();
        for (ReminderPlanner.Planned planned : reminderPlanner.plan(user, settings, localNow)) {
            Optional<ReminderDelivery> delivery;
            try {
                delivery = reminderWriteService.record(user.getId(), planned.type(), localNow.toLocalDate(), planned.body(), now);
            } catch (DataIntegrityViolationException e) {
                continue;
            }
            delivery.ifPresent(sent -> messages.addAll(toMessages(sent, devices)));
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

    static boolean isExpoToken(String pushToken) {
        return pushToken != null && (pushToken.startsWith("ExponentPushToken[") || pushToken.startsWith("ExpoPushToken["));
    }
}
