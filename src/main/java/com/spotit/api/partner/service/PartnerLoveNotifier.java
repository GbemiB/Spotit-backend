package com.spotit.api.partner.service;

import com.spotit.api.common.time.UserClock;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.partner.entity.LoveKind;
import com.spotit.api.partner.entity.LoveOption;
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

    public void notifyOwner(User owner, User partner, LoveKind kind, List<String> labels, String note, LoveOption expression) {
        String title = titleFor(partner, kind, expression);
        String body = bodyFor(kind, labels, note, expression);
        Instant now = Instant.now();
        Optional<ReminderDelivery> delivery;
        try {
            delivery = Optional.of(reminderWriteService.recordEvery(owner.getId(), ReminderType.partner_love, userClock.today(owner), title, body, now));
        } catch (RuntimeException e) {
            log.warn("Could not record partner love for {}: {}", owner.getId(), e.getMessage());
            return;
        }
        if (!owner.isNotifPartnerLoveOn()) {
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

    static String titleFor(User partner, LoveKind kind, LoveOption expression) {
        String name = partner.getFirstName() == null ? "" : partner.getFirstName().strip();
        String who = name.isEmpty() ? "Your partner" : name;
        return switch (kind) {
            case promise -> name.isEmpty() ? ReminderType.partner_love.getTitle() : "A promise from " + name + " ♡";
            case text -> who + " sent you a message";
            case expression -> who + " sent you " + phraseOf(expression) + (expression.getEmoji() == null ? "" : " " + expression.getEmoji());
        };
    }

    static String phraseOf(LoveOption expression) {
        if (expression.getPhrase() != null && !expression.getPhrase().isBlank()) {
            return expression.getPhrase();
        }
        return "“" + expression.getLabel() + "”";
    }

    static String bodyFor(LoveKind kind, List<String> labels, String note, LoveOption expression) {
        if (kind == LoveKind.expression) {
            return "Just because ♡";
        }
        return bodyFor(kind == LoveKind.text ? List.of() : labels, note);
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
