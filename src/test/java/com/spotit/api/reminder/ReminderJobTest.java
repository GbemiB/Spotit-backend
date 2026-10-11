package com.spotit.api.reminder;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.device.service.DeviceWriteService;
import com.spotit.api.reminder.dto.NotificationInboxResponse;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushResult;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.repository.PushTicketRepository;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import com.spotit.api.reminder.service.ReminderReadService;
import com.spotit.api.reminder.service.ReminderWriteService;
import com.spotit.api.user.dto.UpdateNotificationPrefsRequest;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import com.spotit.api.user.service.UserWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The whole reminder path against a real database, with only the call to Expo replaced: registration, the job,
 * the delivery record that prevents duplicates, the preference switches and the inbox.
 *
 * <p>Every user here logged a period on 5 October 2026 with a 28-day cycle, so the next one is predicted for
 * 2 November and Friday 30 October is "3 days before". 06:00 in Lagos (UTC+1) is 05:00 UTC.
 */
@SpringBootTest
@ActiveProfiles("test")
class ReminderJobTest {
    static final LocalDate LAST_PERIOD = LocalDate.of(2026, 10, 5);
    static final Instant LAGOS_0600_3_DAYS_BEFORE = Instant.parse("2026-10-30T05:00:00Z");
    static final String THREE_DAYS = "Your period is predicted to start in 3 days.";

    @Autowired ReminderJob reminderJob;
    @Autowired UserRepository userRepository;
    @Autowired DeviceRepository deviceRepository;
    @Autowired DeviceWriteService deviceWriteService;
    @Autowired UserWriteService userWriteService;
    @Autowired ReminderDeliveryRepository reminderDeliveryRepository;
    @Autowired PushTicketRepository pushTicketRepository;
    @Autowired ReminderReadService reminderReadService;
    @Autowired ReminderWriteService reminderWriteService;
    @Autowired ReminderSettings reminderSettings;
    @MockBean PushSender pushSender;

    int ticketCounter;

    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();
        reminderDeliveryRepository.deleteAll();
        pushTicketRepository.deleteAll();
        // Expo accepts everything unless a test says otherwise.
        when(pushSender.send(anyList())).thenAnswer(inv -> inv.<List<PushMessage>>getArgument(0).stream()
                .map(message -> new PushResult(message.to(), "ticket-" + ticketCounter++, null)).toList());
    }

    private UUID user(String zone, String... pushTokens) {
        UUID userId = userRepository.save(User.builder().firstName("Jane").lastName("Doe")
                .email("reminders-" + UUID.randomUUID() + "@example.com").passwordHash("x").emailVerified(true).onboarded(true)
                .cycleLength(28).periodLength(5).lastPeriodDate(LAST_PERIOD).themePref(ThemePref.dark)
                .notifPeriod(true).notifOvulation(true).notifDailyLog(true).notifDigest(false).build()).getId();
        for (String pushToken : pushTokens) {
            deviceWriteService.register(userId, new RegisterDeviceRequest(pushToken, "android", zone));
        }
        return userId;
    }

    private static String token(String name) {
        return "ExponentPushToken[" + name + "]";
    }

    /** Every message handed to the push service so far, across runs. */
    private List<PushMessage> sentMessages() {
        List<PushMessage> all = new ArrayList<>();
        mockingDetails(pushSender).getInvocations().stream()
                .filter(invocation -> invocation.getMethod().getName().equals("send"))
                .forEach(invocation -> all.addAll(invocation.<List<PushMessage>>getArgument(0)));
        return all;
    }

    @Test
    void aReminderIsSentAtNineInTheUsersOwnTimeAndOnlyOnce() {
        UUID userId = user("Africa/Lagos", token("lagos"));

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.minusSeconds(60));
        assertThat(sentMessages()).isEmpty();

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);
        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(15 * 60));
        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(30 * 60));

        List<PushMessage> messages = sentMessages();
        assertThat(messages).hasSize(1);
        assertThat(messages.get(0).to()).isEqualTo(token("lagos"));
        assertThat(messages.get(0).title()).isEqualTo("Period reminder");
        assertThat(messages.get(0).body()).isEqualTo(THREE_DAYS);
        assertThat(messages.get(0).data()).containsEntry("type", "period_upcoming").containsEntry("screen", "home").containsKey("id");
        assertThat(pushTicketRepository.count()).isEqualTo(1);

        NotificationInboxResponse inbox = reminderReadService.getInbox(userId);
        assertThat(inbox.unreadCount()).isEqualTo(1);
        assertThat(inbox.items()).hasSize(1);
        assertThat(inbox.items().get(0).body()).isEqualTo(THREE_DAYS);
        assertThat(inbox.items().get(0).id()).hasToString(messages.get(0).data().get("id"));

        reminderWriteService.markAllRead(userId);
        assertThat(reminderReadService.getInbox(userId).unreadCount()).isZero();
        assertThat(reminderReadService.getInbox(userId).items().get(0).read()).isTrue();
    }

    @Test
    void theCountdownContinuesOnTheFollowingDays() {
        user("Africa/Lagos", token("lagos"));

        for (int day = 0; day <= 3; day++) {
            reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(day * 86_400L));
        }

        assertThat(sentMessages()).extracting(PushMessage::body).containsExactly(
                THREE_DAYS,
                "Your period is predicted to start in 2 days.",
                "Your period is predicted to start tomorrow.",
                "Your period may start today. Tap to log it.");
    }

    @Test
    void usersInOtherTimeZonesAreReachedAtTheirOwnSixOClock() {
        user("Africa/Lagos", token("lagos"));
        user("America/New_York", token("newyork"));

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);
        assertThat(sentMessages()).extracting(PushMessage::to).containsExactly(token("lagos"));

        // 06:00 in New York on 30 October 2026 (daylight time, UTC-4) is 10:00 UTC.
        reminderJob.run(Instant.parse("2026-10-30T10:00:00Z"));
        assertThat(sentMessages()).extracting(PushMessage::to).containsExactly(token("lagos"), token("newyork"));
    }

    @Test
    void switchingThePeriodReminderOffStopsItAndLeavesOvulationArriving() {
        UUID userId = user("Africa/Lagos", token("lagos"));
        userWriteService.updateNotificationPrefs(userId, new UpdateNotificationPrefsRequest(false, null, null, null, null, null, null, null));

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);
        assertThat(sentMessages()).isEmpty();

        // 18 October is her predicted ovulation day.
        reminderJob.run(Instant.parse("2026-10-18T05:00:00Z"));
        assertThat(sentMessages()).extracting(PushMessage::body).containsExactly("Today is your predicted ovulation day.");
    }

    @Test
    void uncheckingACountdownDaySkipsOnlyThatDay() {
        UUID userId = user("Africa/Lagos", token("lagos"));
        userWriteService.updateNotificationPrefs(userId,
                new UpdateNotificationPrefsRequest(null, null, null, null, null, null, List.of(2, 0), null));

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);
        assertThat(sentMessages()).isEmpty();

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(86_400));
        assertThat(sentMessages()).extracting(PushMessage::body).containsExactly("Your period is predicted to start in 2 days.");
    }

    @Test
    void aChangedReminderTimeIsHonoured() {
        UUID userId = user("Africa/Lagos", token("lagos"));
        userWriteService.updateNotificationPrefs(userId,
                new UpdateNotificationPrefsRequest(null, null, null, null, "08:30", null, null, null));

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);
        reminderJob.run(Instant.parse("2026-10-30T07:15:00Z"));
        assertThat(sentMessages()).isEmpty();

        reminderJob.run(Instant.parse("2026-10-30T07:30:00Z"));
        assertThat(sentMessages()).hasSize(1);
    }

    @Test
    void aRunMissedDuringARestartIsCaughtUpWithinTwoHoursButNotLater() {
        user("Africa/Lagos", token("ontime"));
        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(90 * 60));
        assertThat(sentMessages()).hasSize(1);

        deviceRepository.deleteAll();
        user("Africa/Lagos", token("toolate"));
        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(120 * 60));
        assertThat(sentMessages()).hasSize(1);
    }

    @Test
    void everyPhoneOnTheAccountGetsTheReminderButAnOldDeviceIdRowDoesNot() {
        // Version 1.0.0 registered a device id where the push address now goes.
        user("Africa/Lagos", token("phone"), token("tablet"), "a1b2c3d4-device-id");

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);

        assertThat(sentMessages()).extracting(PushMessage::to).containsExactlyInAnyOrder(token("phone"), token("tablet"));
        assertThat(reminderDeliveryRepository.count()).isEqualTo(1);
    }

    @Test
    void aUserWhoSignedOutGetsNothing() {
        UUID userId = user("Africa/Lagos", token("lagos"));
        deviceWriteService.unregister(token("lagos"));

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);

        assertThat(sentMessages()).isEmpty();
        assertThat(reminderReadService.getInbox(userId).items()).isEmpty();
    }

    @Test
    void theScheduleComesFromTheProperties() {
        assertThat(reminderSettings.enabled()).isTrue();
        assertThat(reminderSettings.morningTime()).isEqualTo(LocalTime.of(6, 0));
        assertThat(reminderSettings.eveningTime()).isEqualTo(LocalTime.of(20, 0));
        assertThat(reminderSettings.digestDay()).isEqualTo(DayOfWeek.SUNDAY);
        assertThat(reminderSettings.digestTime()).isEqualTo(LocalTime.of(19, 0));
        assertThat(reminderSettings.periodDaysBefore()).containsExactly(3, 2, 1, 0);
        assertThat(reminderSettings.ovulationDaysBefore()).containsExactly(3, 2, 1, 0);
        assertThat(reminderSettings.maxDaysBefore()).isEqualTo(3);
        assertThat(reminderSettings.catchUpMinutes()).isEqualTo(120);
    }

    @Test
    void aPhoneThatUninstalledTheAppIsForgotten() {
        user("Africa/Lagos", token("gone"), token("kept"));
        when(pushSender.send(anyList())).thenAnswer(inv -> inv.<List<PushMessage>>getArgument(0).stream()
                .map(message -> message.to().equals(token("gone"))
                        ? new PushResult(message.to(), null, PushResult.DEVICE_NOT_REGISTERED)
                        : new PushResult(message.to(), "ticket-kept", null)).toList());

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);

        assertThat(deviceRepository.findByPushToken(token("gone"))).isEmpty();
        assertThat(deviceRepository.findByPushToken(token("kept"))).isPresent();
    }

    @Test
    void receiptsAreCollectedOnALaterRunAndDeadDevicesRemoved() {
        user("Africa/Lagos", token("lagos"));
        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE);
        String ticketId = pushTicketRepository.findAll().get(0).getTicketId();
        when(pushSender.receipts(List.of(ticketId))).thenReturn(Map.of(ticketId, PushResult.DEVICE_NOT_REGISTERED));

        // Too soon: Expo asks for about 15 minutes before a receipt is fetched.
        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(10 * 60));
        verify(pushSender, never()).receipts(anyList());

        reminderJob.run(LAGOS_0600_3_DAYS_BEFORE.plusSeconds(16 * 60));
        assertThat(pushTicketRepository.count()).isZero();
        assertThat(deviceRepository.findByPushToken(token("lagos"))).isEmpty();
    }
}
