package com.spotit.api.reminder.service;

import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.reminder.entity.PushTicket;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.push.PushResult;
import com.spotit.api.reminder.repository.PushTicketRepository;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderWriteServiceImplTest {
    static final Instant NOW = Instant.parse("2026-10-30T08:00:00Z");
    static final LocalDate DAY = LocalDate.of(2026, 10, 30);

    @Mock ReminderDeliveryRepository reminderDeliveryRepository;
    @Mock PushTicketRepository pushTicketRepository;
    @Mock DeviceRepository deviceRepository;
    @InjectMocks ReminderWriteServiceImpl service;

    @Captor ArgumentCaptor<List<PushTicket>> tickets;

    UUID userId = UUID.randomUUID();

    @Test
    void recordSavesTheDeliveryWithTheTypesTitle() {
        when(reminderDeliveryRepository.existsByUserIdAndTypeAndForDate(userId, ReminderType.period_upcoming, DAY)).thenReturn(false);
        when(reminderDeliveryRepository.saveAndFlush(any(ReminderDelivery.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<ReminderDelivery> delivery = service.record(userId, ReminderType.period_upcoming, DAY, "In 3 days.", NOW);

        assertThat(delivery).isPresent();
        assertThat(delivery.get().getUserId()).isEqualTo(userId);
        assertThat(delivery.get().getTitle()).isEqualTo("Period reminder");
        assertThat(delivery.get().getBody()).isEqualTo("In 3 days.");
        assertThat(delivery.get().getForDate()).isEqualTo(DAY);
        assertThat(delivery.get().getSentAt()).isEqualTo(NOW);
        assertThat(delivery.get().isRead()).isFalse();
    }

    @Test
    void recordReturnsNothingWhenTheReminderWasAlreadySentThatDay() {
        when(reminderDeliveryRepository.existsByUserIdAndTypeAndForDate(userId, ReminderType.period_upcoming, DAY)).thenReturn(true);

        assertThat(service.record(userId, ReminderType.period_upcoming, DAY, "In 3 days.", NOW)).isEmpty();
        verify(reminderDeliveryRepository, never()).saveAndFlush(any());
    }

    @Test
    void markAllReadUpdatesTheUsersRows() {
        service.markAllRead(userId);

        verify(reminderDeliveryRepository).markAllRead(userId);
    }

    @Test
    void sendResultsKeepTicketsAndForgetDevicesThatAreGone() {
        service.applySendResults(List.of(
                new PushResult("token-a", "ticket-a", null),
                new PushResult("token-b", null, PushResult.DEVICE_NOT_REGISTERED),
                new PushResult("token-c", null, "MessageRateExceeded")), NOW);

        verify(pushTicketRepository).saveAll(tickets.capture());
        assertThat(tickets.getValue()).hasSize(1);
        assertThat(tickets.getValue().get(0).getTicketId()).isEqualTo("ticket-a");
        assertThat(tickets.getValue().get(0).getPushToken()).isEqualTo("token-a");
        assertThat(tickets.getValue().get(0).getCreatedAt()).isEqualTo(NOW);
        verify(deviceRepository).deleteByPushTokenIn(List.of("token-b"));
    }

    @Test
    void sendResultsWithNoDeadDevicesDeleteNothing() {
        service.applySendResults(List.of(new PushResult("token-a", "ticket-a", null)), NOW);

        verify(deviceRepository, never()).deleteByPushTokenIn(any());
    }

    @Test
    void ticketsAreDueFifteenMinutesAfterSending() {
        service.ticketsDue(NOW);

        verify(pushTicketRepository).findTop1000ByCreatedAtBeforeOrderByCreatedAtAsc(NOW.minus(Duration.ofMinutes(15)));
    }

    @Test
    void receiptsSettleTicketsAndForgetUninstalledDevices() {
        PushTicket delivered = ticket("delivered", "token-a", NOW.minus(Duration.ofMinutes(20)));
        PushTicket gone = ticket("gone", "token-b", NOW.minus(Duration.ofMinutes(20)));
        PushTicket pending = ticket("pending", "token-c", NOW.minus(Duration.ofMinutes(20)));
        PushTicket stale = ticket("stale", "token-d", NOW.minus(Duration.ofHours(25)));

        service.applyReceipts(List.of(delivered, gone, pending, stale),
                Map.of("delivered", "", "gone", PushResult.DEVICE_NOT_REGISTERED), NOW);

        verify(deviceRepository).deleteByPushTokenIn(List.of("token-b"));
        // "pending" has no receipt yet and stays for the next run; "stale" will never get one.
        verify(pushTicketRepository).deleteAll(List.of(delivered, gone, stale));
    }

    @Test
    void purgeDropsInboxRowsOlderThanNinetyDays() {
        service.purgeOld(NOW);

        verify(reminderDeliveryRepository).deleteBySentAtBefore(NOW.minus(Duration.ofDays(90)));
    }

    private static PushTicket ticket(String ticketId, String pushToken, Instant createdAt) {
        return PushTicket.builder().ticketId(ticketId).pushToken(pushToken).createdAt(createdAt).build();
    }

    @Test
    void anUnreadableReceiptIsLoggedAndTheTicketSettledWithoutForgettingTheDevice() {
        PushTicket throttled = ticket("throttled", "token-a", NOW.minus(Duration.ofMinutes(20)));

        service.applyReceipts(List.of(throttled), Map.of("throttled", "MessageRateExceeded"), NOW);

        verify(deviceRepository, never()).deleteByPushTokenIn(any());
        verify(pushTicketRepository).deleteAll(List.of(throttled));
    }

    @Test
    void ticketsWithNoReceiptYetAreAllKept() {
        PushTicket pending = ticket("pending", "token-a", NOW.minus(Duration.ofMinutes(20)));

        service.applyReceipts(List.of(pending), Map.of(), NOW);

        verify(deviceRepository, never()).deleteByPushTokenIn(any());
        verify(pushTicketRepository).deleteAll(List.of());
    }
}
