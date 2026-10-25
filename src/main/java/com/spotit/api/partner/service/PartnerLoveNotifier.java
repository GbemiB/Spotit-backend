package com.spotit.api.partner.service;

import com.spotit.api.common.time.UserClock;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.service.ReminderWriteService;
import com.spotit.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PartnerLoveNotifier {
    static final int BODY_MAX = 300;

    private final ReminderWriteService reminderWriteService;
    private final DeviceRepository deviceRepository;
    private final PushSender pushSender;
    private final UserClock userClock;

    public void notifyOwner(User owner, User partner, List<String> labels, String note) {
        String title = titleFor(partner);
        String body = bodyFor(labels, note);
        Instant now = Instant.now();
        Optional<ReminderDelivery> delivery;
        try {
            delivery = reminderWriteService.record(owner.getId(), ReminderType.partner_love, userClock.today(owner), title, body, now);
        } catch (RuntimeException e) {
            log.warn("Could not record partner love for {}: {}", owner.getId(), e.getMessage());
            return;
        }
        if (delivery.isEmpty() || !owner.isNotifPartnerLoveOn()) {
            return;
        }
        List<PushMessage> messages = deviceRepository.findByUserId(owner.getId()).stream()
                .map(Device::getPushToken)
                .filter(PartnerLoveNotifier::isExpoToken)
                .map(token -> new PushMessage(token, title, body, Map.of(
                        "type", ReminderType.partner_love.name(), "screen", ReminderType.partner_love.getScreen(),
                        "id", delivery.get().getId().toString())))
                .toList();
        if (messages.isEmpty()) {
            return;
        }
        Runnable send = () -> {
            try {
                reminderWriteService.applySendResults(pushSender.send(messages), Instant.now());
            } catch (RuntimeException e) {
                log.warn("Could not push partner love to {}: {}", owner.getId(), e.getMessage());
            }
        };
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send.run();
                }
            });
        } else {
            send.run();
        }
    }

    static String titleFor(User partner) {
        String name = partner.getFirstName() == null ? "" : partner.getFirstName().strip();
        return name.isEmpty() ? ReminderType.partner_love.getTitle() : "A promise from " + name + " ♡";
    }

    static String bodyFor(List<String> labels, String note) {
        String things = String.join(" · ", labels);
        String quoted = note == null || note.isBlank() ? "" : "“" + note.strip() + "”";
        String body = things.isEmpty() ? quoted : quoted.isEmpty() ? things : things + " — " + quoted;
        return body.length() > BODY_MAX ? body.substring(0, BODY_MAX - 1) + "…" : body;
    }

    private static boolean isExpoToken(String pushToken) {
        return pushToken != null && (pushToken.startsWith("ExponentPushToken[") || pushToken.startsWith("ExpoPushToken["));
    }
}
