package com.spotit.api.reminder.service;

import com.spotit.api.reminder.dto.NotificationInboxResponse;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderReadServiceImplTest {
    @Mock ReminderDeliveryRepository reminderDeliveryRepository;
    @InjectMocks ReminderReadServiceImpl service;

    @Test
    void theInboxListsSentRemindersWithTheScreenATapOpens() {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Instant sentAt = Instant.parse("2026-10-30T08:00:00Z");
        when(reminderDeliveryRepository.findTop50ByUserIdOrderBySentAtDesc(userId)).thenReturn(List.of(ReminderDelivery.builder()
                .id(id).userId(userId).type(ReminderType.period_upcoming).forDate(LocalDate.of(2026, 10, 30))
                .title("Period reminder").body("Your period is predicted to start in 3 days.").sentAt(sentAt).build()));
        when(reminderDeliveryRepository.countByUserIdAndReadFalse(userId)).thenReturn(1L);

        NotificationInboxResponse inbox = service.getInbox(userId);

        assertThat(inbox.unreadCount()).isEqualTo(1);
        assertThat(inbox.items()).hasSize(1);
        assertThat(inbox.items().get(0).id()).isEqualTo(id);
        assertThat(inbox.items().get(0).type()).isEqualTo("period_upcoming");
        assertThat(inbox.items().get(0).title()).isEqualTo("Period reminder");
        assertThat(inbox.items().get(0).body()).isEqualTo("Your period is predicted to start in 3 days.");
        assertThat(inbox.items().get(0).screen()).isEqualTo("home");
        assertThat(inbox.items().get(0).sentAt()).isEqualTo(sentAt);
        assertThat(inbox.items().get(0).read()).isFalse();
    }
}
