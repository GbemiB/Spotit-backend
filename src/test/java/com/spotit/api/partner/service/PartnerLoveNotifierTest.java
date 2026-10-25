package com.spotit.api.partner.service;

import com.spotit.api.common.time.UserClock;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.partner.entity.LoveOption;
import com.spotit.api.partner.entity.LoveKind;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.service.ReminderWriteService;
import com.spotit.api.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerLoveNotifierTest {
    @Mock ReminderWriteService reminderWriteService;
    @Mock DeviceRepository deviceRepository;
    @Mock PushSender pushSender;
    @Mock UserClock userClock;
    @InjectMocks PartnerLoveNotifier notifier;

    UUID ownerId = UUID.randomUUID();
    LocalDate today = LocalDate.of(2026, 10, 25);
    User partner = User.builder().id(UUID.randomUUID()).firstName("Tunde").build();

    @Test
    void bodyJoinsWhatHeDidAndHisNote() {
        assertThat(PartnerLoveNotifier.bodyFor(List.of("Listen to her", "Tea & treats"), "Proud of you"))
                .isEqualTo("Listen to her · Tea & treats — “Proud of you”");
        assertThat(PartnerLoveNotifier.bodyFor(List.of("Back rub"), null)).isEqualTo("Back rub");
        assertThat(PartnerLoveNotifier.bodyFor(List.of(), "Thinking of you")).isEqualTo("“Thinking of you”");
    }

    @Test
    void titleUsesHisName() {
        assertThat(PartnerLoveNotifier.titleFor(partner, LoveKind.promise, null)).isEqualTo("A promise from Tunde ♡");
        assertThat(PartnerLoveNotifier.titleFor(User.builder().build(), LoveKind.promise, null)).isEqualTo("A promise from your partner");
        assertThat(PartnerLoveNotifier.titleFor(partner, LoveKind.text, null)).isEqualTo("Tunde sent you a message");
        assertThat(PartnerLoveNotifier.titleFor(partner, LoveKind.expression, LoveOption.builder().id("hug").label("Hug").emoji("🤗").phrase("a hug").build())).isEqualTo("Tunde sent you a hug 🤗");
        assertThat(PartnerLoveNotifier.bodyFor(LoveKind.text, List.of("Back rub"), "Proud of you", null)).isEqualTo("“Proud of you”");
        assertThat(PartnerLoveNotifier.bodyFor(LoveKind.expression, List.of(), null, LoveOption.builder().id("kiss").label("Kiss").build())).isEqualTo("Just because ♡");
    }

    @Test
    void itLandsInHerInboxAndOnHerPhone() {
        User owner = User.builder().id(ownerId).build();
        when(userClock.today(owner)).thenReturn(today);
        when(reminderWriteService.recordEvery(eq(ownerId), eq(ReminderType.partner_love), eq(today), eq("A promise from Tunde ♡"), eq("Listen to her"), any()))
                .thenReturn(ReminderDelivery.builder().id(UUID.randomUUID()).type(ReminderType.partner_love).build());
        when(deviceRepository.findByUserId(ownerId)).thenReturn(List.of(
                Device.builder().userId(ownerId).pushToken("ExponentPushToken[abc]").build(),
                Device.builder().userId(ownerId).pushToken(null).build()));

        notifier.notifyOwner(owner, partner, LoveKind.promise, List.of("Listen to her"), null, null);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PushMessage>> sent = ArgumentCaptor.forClass(List.class);
        verify(pushSender).send(sent.capture());
        assertThat(sent.getValue()).hasSize(1);
        assertThat(sent.getValue().get(0).title()).isEqualTo("A promise from Tunde ♡");
        assertThat(sent.getValue().get(0).data()).containsEntry("screen", "home");
    }

    @Test
    void sheCanTurnThePushOffButKeepsTheInbox() {
        User owner = User.builder().id(ownerId).notifPartnerLove(false).build();
        when(userClock.today(owner)).thenReturn(today);
        when(reminderWriteService.recordEvery(eq(ownerId), eq(ReminderType.partner_love), eq(today), any(), any(), any()))
                .thenReturn(ReminderDelivery.builder().id(UUID.randomUUID()).type(ReminderType.partner_love).build());

        notifier.notifyOwner(owner, partner, LoveKind.promise, List.of("Listen to her"), null, null);

        verify(pushSender, never()).send(anyList());
    }

    @Test
    void afterThreeSendsHerPhoneGetsOneUpdatingNotification() {
        User owner = User.builder().id(ownerId).build();
        when(userClock.today(owner)).thenReturn(today);
        when(reminderWriteService.recordEvery(eq(ownerId), eq(ReminderType.partner_love), eq(today), any(), any(), any()))
                .thenReturn(ReminderDelivery.builder().id(UUID.randomUUID()).type(ReminderType.partner_love).build());
        when(reminderWriteService.countFor(ownerId, ReminderType.partner_love, today)).thenReturn(5L);
        when(deviceRepository.findByUserId(ownerId)).thenReturn(List.of(Device.builder().userId(ownerId).pushToken("ExponentPushToken[abc]").build()));

        notifier.notifyOwner(owner, partner, LoveKind.promise, List.of("Back rub"), null, null);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PushMessage>> sent = ArgumentCaptor.forClass(List.class);
        verify(pushSender).send(sent.capture());
        PushMessage message = sent.getValue().get(0);
        assertThat(message.title()).isEqualTo("Tunde sent you more love ♡");
        assertThat(message.body()).isEqualTo("2 more today");
        assertThat(message.collapseId()).isEqualTo("partner-love-" + ownerId + "-" + today);
        verify(reminderWriteService).recordEvery(eq(ownerId), eq(ReminderType.partner_love), eq(today), eq("A promise from Tunde ♡"),
                eq("Back rub"), any());
    }

    @Test
    void theFirstThreeAreSeparate() {
        User owner = User.builder().id(ownerId).build();
        when(userClock.today(owner)).thenReturn(today);
        when(reminderWriteService.recordEvery(eq(ownerId), eq(ReminderType.partner_love), eq(today), any(), any(), any()))
                .thenReturn(ReminderDelivery.builder().id(UUID.randomUUID()).type(ReminderType.partner_love).build());
        when(reminderWriteService.countFor(ownerId, ReminderType.partner_love, today)).thenReturn(3L);
        when(deviceRepository.findByUserId(ownerId)).thenReturn(List.of(Device.builder().userId(ownerId).pushToken("ExponentPushToken[abc]").build()));

        notifier.notifyOwner(owner, partner, LoveKind.text, List.of(), "Proud of you", null);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<PushMessage>> sent = ArgumentCaptor.forClass(List.class);
        verify(pushSender).send(sent.capture());
        assertThat(sent.getValue().get(0).title()).isEqualTo("Tunde sent you a message");
        assertThat(sent.getValue().get(0).collapseId()).isNull();
    }
}
