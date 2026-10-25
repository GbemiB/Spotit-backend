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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PartnerLoveNotifier {
    static final int BODY_MAX = 300;
    static final int SEPARATE_PUSHES = 3;

    private final ReminderWriteService reminderWriteService;
    private final DeviceRepository deviceRepository;
    private final PushSender pushSender;
    private final UserClock userClock;

    public void notifyOwner(User owner, User partner, LoveKind kind, List<String> labels, String note, LoveOption expression) {
        String title = titleFor(partner, kind, expression);
        String body = bodyFor(kind, labels, note, expression);
        Instant now = Instant.now();
        LocalDate day = userClock.today(owner);
        Optional<ReminderDelivery> delivery;
        long sentToday;
        try {
            delivery = Optional.of(reminderWriteService.recordEvery(owner.getId(), ReminderType.partner_love, day, title, body, now));
            sentToday = reminderWriteService.countFor(owner.getId(), ReminderType.partner_love, day);
        } catch (RuntimeException e) {
            log.warn("Could not record partner love for {}: {}", owner.getId(), e.getMessage());
            return;
        }
        if (!owner.isNotifPartnerLoveOn()) {
            return;
        }
        boolean grouped = sentToday > SEPARATE_PUSHES;
        String pushTitle = grouped ? moreTitleFor(partner) : title;
        String pushBody = grouped ? (sentToday - SEPARATE_PUSHES) + " more today" : body;
        String collapseId = grouped ? "partner-love-" + owner.getId() + "-" + day : null;
        push(owner.getId(), pushTitle, pushBody, ReminderType.partner_love, delivery.get().getId().toString(), collapseId);
    }

    public void notifyPartner(User partner, User owner, LoveKind kind, LoveOption expression, boolean heart, String note) {
        String name = owner.getFirstName() == null || owner.getFirstName().isBlank() ? "Your partner" : owner.getFirstName().strip();
        String title = name + " felt your love ♡";
        String about = "on " + aboutFor(kind, expression);
        String body = note == null ? cut(capitalise(about)) : cut("“" + note + "” – " + about);
        Instant now = Instant.now();
        ReminderDelivery delivery;
        try {
            delivery = reminderWriteService.recordEvery(partner.getId(), ReminderType.partner_reply, userClock.today(partner), title, body, now);
        } catch (RuntimeException e) {
            log.warn("Could not record partner reply for {}: {}", partner.getId(), e.getMessage());
            return;
        }
        if (!partner.isNotifPartnerLoveOn()) {
            return;
        }
        push(partner.getId(), title, body, ReminderType.partner_reply, delivery.getId().toString(), null);
    }

    static String aboutFor(LoveKind kind, LoveOption expression) {
        return switch (kind) {
            case promise -> "your promise";
            case text -> "your message";
            case expression -> {
                String phrase = expression == null ? "love" : phraseOf(expression);
                String yours = phrase.startsWith("a ") ? "your " + phrase.substring(2) : "your " + phrase;
                yield expression != null && expression.getEmoji() != null ? yours + " " + expression.getEmoji() : yours;
            }
        };
    }

    private static String capitalise(String value) {
        return value.isEmpty() ? value : Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private static String cut(String body) {
        return body.length() > BODY_MAX ? body.substring(0, BODY_MAX - 1) + "…" : body;
    }

    private void push(java.util.UUID userId, String title, String body, ReminderType type, String deliveryId, String collapseId) {
        List<PushMessage> messages = deviceRepository.findByUserId(userId).stream()
                .map(Device::getPushToken)
                .filter(PartnerLoveNotifier::isExpoToken)
                .map(token -> new PushMessage(token, title, body, Map.of(
                        "type", type.name(), "screen", type.getScreen(), "id", deliveryId), collapseId))
                .toList();
        if (messages.isEmpty()) {
            return;
        }
        Runnable send = () -> {
            try {
                reminderWriteService.applySendResults(pushSender.send(messages), Instant.now());
            } catch (RuntimeException e) {
                log.warn("Could not push {} to {}: {}", type, userId, e.getMessage());
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

    static String moreTitleFor(User partner) {
        String name = partner.getFirstName() == null ? "" : partner.getFirstName().strip();
        return (name.isEmpty() ? "Your partner" : name) + " sent you more love ♡";
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
            return "A little love for you ♡";
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
