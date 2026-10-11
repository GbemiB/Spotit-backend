package com.spotit.api.reminder;

import com.spotit.api.reminder.service.ReminderWriteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReminderSchedulerTest {
    @Mock ReminderJob reminderJob;
    @Mock ReminderWriteService reminderWriteService;
    @InjectMocks ReminderScheduler scheduler;

    @Test
    void eachTickRunsTheJobWithTheCurrentTime() {
        Instant before = Instant.now();

        scheduler.sendDueReminders();

        verify(reminderJob).run(argThat(now -> !now.isBefore(before) && Duration.between(before, now).getSeconds() < 5));
    }

    @Test
    void aFailingRunIsLoggedSoTheNextTickStillHappens() {
        doThrow(new IllegalStateException("database down")).when(reminderJob).run(any());

        assertThatCode(scheduler::sendDueReminders).doesNotThrowAnyException();
    }

    @Test
    void theNightlyPurgeUsesTheCurrentTime() {
        Instant before = Instant.now();

        scheduler.purgeOldReminders();

        verify(reminderWriteService).purgeOld(argThat(now -> {
            assertThat(now).isAfterOrEqualTo(before);
            return true;
        }));
    }
}
