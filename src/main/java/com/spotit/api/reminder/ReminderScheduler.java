package com.spotit.api.reminder;

import com.spotit.api.reminder.service.ReminderWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderScheduler {
    private final ReminderJob reminderJob;
    private final ReminderWriteService reminderWriteService;

    // Every 15 minutes: that is the accuracy a reminder time is honoured with. Tests set the cron to "-" (off)
    // and drive ReminderJob with their own clock.
    @Scheduled(cron = "${spotit.reminders.cron:0 */15 * * * *}")
    public void sendDueReminders() {
        try {
            reminderJob.run(Instant.now());
        } catch (RuntimeException e) {
            log.error("Reminder job failed", e);
        }
    }

    @Scheduled(cron = "0 30 3 * * *")
    public void purgeOldReminders() {
        reminderWriteService.purgeOld(Instant.now());
    }
}
