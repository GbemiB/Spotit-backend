package com.spotit.api.reminder;

import com.spotit.api.configuration.ReminderSettings;
import com.spotit.api.configuration.ReminderSettingsFixture;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.reminder.entity.PushTicket;
import com.spotit.api.reminder.entity.ReminderDelivery;
import com.spotit.api.reminder.entity.ReminderType;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushResult;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.service.PartnerReminderPlanner;
import com.spotit.api.reminder.service.ReminderPlanner;
import com.spotit.api.reminder.service.ReminderPlanner.Planned;
import com.spotit.api.reminder.service.ReminderWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderJobUnitTest {
    static final Instant NOW = Instant.parse("2026-10-30T05:00:00Z");
    static final Planned THREE_DAYS = new Planned(ReminderType.period_upcoming, "Your period is predicted to start in 3 days.");

    @Mock UserRepository userRepository;
    @Mock DeviceRepository deviceRepository;
    @Mock ReminderPlanner reminderPlanner;
    @Mock ReminderWriteService reminderWriteService;
    @Mock PushSender pushSender;
    @Mock PartnerLinkRepository partnerLinkRepository;
    @Mock PartnerReminderPlanner partnerReminderPlanner;
    ReminderSettings settings = ReminderSettingsFixture.defaults();
    ReminderJob job;

    @BeforeEach
    void setUp() {
        job = new ReminderJob(settings, userRepository, deviceRepository, reminderPlanner, reminderWriteService, pushSender,
                partnerLinkRepository, partnerReminderPlanner);
        lenient().when(reminderWriteService.ticketsDue(any())).thenReturn(List.of());
        lenient().when(reminderWriteService.record(any(), any(), any(), any(), any())).thenAnswer(inv -> Optional.of(delivery(
                inv.getArgument(0), inv.getArgument(1), inv.getArgument(2), inv.getArgument(3))));
        lenient().when(pushSender.send(anyList())).thenReturn(List.of());
    }

    private static User user(String timeZone) {
        return User.builder().id(UUID.randomUUID()).timeZone(timeZone).build();
    }

    private static Device device(User user, String pushToken, String timeZone) {
        return Device.builder().id(UUID.randomUUID()).userId(user.getId()).pushToken(pushToken).timeZone(timeZone).build();
    }

    private static ReminderDelivery delivery(UUID userId, ReminderType type, LocalDate forDate, String body) {
        return ReminderDelivery.builder().id(UUID.randomUUID()).userId(userId).type(type).forDate(forDate)
                .title(type.getTitle()).body(body).sentAt(NOW).build();
    }

    private void given(List<User> users, Device... devices) {
        when(userRepository.findReminderCandidates()).thenReturn(users);
        when(deviceRepository.findByUserIdIn(anyList())).thenReturn(List.of(devices));
    }

    private LocalDateTime localTimePlannedFor(User user) {
        ArgumentCaptor<LocalDateTime> localNow = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(reminderPlanner).plan(eq(user), eq(settings), localNow.capture());
        return localNow.getValue();
    }

    @SuppressWarnings("unchecked")
    private List<PushMessage> sentMessages() {
        ArgumentCaptor<List<PushMessage>> messages = ArgumentCaptor.forClass(List.class);
        verify(pushSender).send(messages.capture());
        return messages.getValue();
    }

    @Test
    void nothingRunsWhenTheOperatorHasSwitchedRemindersOff() {
        job = new ReminderJob(ReminderSettingsFixture.disabled(), userRepository, deviceRepository, reminderPlanner,
                reminderWriteService, pushSender, partnerLinkRepository, partnerReminderPlanner);

        job.run(NOW);

        verifyNoInteractions(userRepository, deviceRepository, reminderPlanner, reminderWriteService, pushSender);
    }

    @Test
    void theAccountTimeZoneIsUsedFirst() {
        User user = user("Africa/Lagos");
        given(List.of(user), device(user, "ExponentPushToken[a]", "Europe/London"));

        job.run(NOW);

        assertThat(localTimePlannedFor(user)).isEqualTo(LocalDateTime.of(2026, 10, 30, 6, 0));
    }

    @Test
    void aDeviceTimeZoneCoversAnAccountWithoutOne() {
        User user = user(null);
        given(List.of(user), device(user, "ExponentPushToken[a]", null), device(user, "ExponentPushToken[b]", "America/New_York"));

        job.run(NOW);

        assertThat(localTimePlannedFor(user)).isEqualTo(LocalDateTime.of(2026, 10, 30, 1, 0));
    }

    @Test
    void noTimeZoneAnywhereFallsBackToUtc() {
        User user = user(null);
        given(List.of(user), device(user, "ExponentPushToken[a]", null));

        job.run(NOW);

        assertThat(localTimePlannedFor(user)).isEqualTo(LocalDateTime.of(2026, 10, 30, 5, 0));
    }

    @Test
    void anUnreadableTimeZoneFallsBackToUtcInsteadOfFailing() {
        User user = user("Mars/Olympus");
        given(List.of(user), device(user, "ExponentPushToken[a]", null));

        job.run(NOW);

        assertThat(localTimePlannedFor(user)).isEqualTo(LocalDateTime.of(2026, 10, 30, 5, 0));
    }

    @Test
    void aUserWithNoPushAddressIsNotPlanned() {
        User withoutPhone = user("UTC");
        User withOldDeviceId = user("UTC");
        given(List.of(withoutPhone, withOldDeviceId), device(withOldDeviceId, "3f2a-device-id", "UTC"));

        job.run(NOW);

        verifyNoInteractions(reminderPlanner);
        verify(pushSender, never()).send(anyList());
    }

    @Test
    void oneMessageGoesToEachPhoneWithTheTapTarget() {
        User user = user("Africa/Lagos");
        given(List.of(user), device(user, "ExponentPushToken[a]", null), device(user, "ExpoPushToken[b]", null));
        when(reminderPlanner.plan(any(), any(), any())).thenReturn(List.of(THREE_DAYS));

        job.run(NOW);

        List<PushMessage> messages = sentMessages();
        assertThat(messages).extracting(PushMessage::to).containsExactly("ExponentPushToken[a]", "ExpoPushToken[b]");
        assertThat(messages).allSatisfy(message -> {
            assertThat(message.title()).isEqualTo("Period reminder");
            assertThat(message.body()).isEqualTo(THREE_DAYS.body());
            assertThat(message.data()).containsEntry("type", "period_upcoming").containsEntry("screen", "home").containsKey("id");
        });
        verify(reminderWriteService).record(user.getId(), ReminderType.period_upcoming, LocalDate.of(2026, 10, 30), THREE_DAYS.body(), NOW);
    }

    @Test
    void theReminderDateIsTheUsersLocalDateNotTheServers() {
        Instant noonUtc = Instant.parse("2026-10-30T12:00:00Z");
        User user = user("Pacific/Kiritimati");
        given(List.of(user), device(user, "ExponentPushToken[a]", null));
        when(reminderPlanner.plan(any(), any(), any())).thenReturn(List.of(THREE_DAYS));

        job.run(noonUtc);

        verify(reminderWriteService).record(user.getId(), ReminderType.period_upcoming, LocalDate.of(2026, 10, 31), THREE_DAYS.body(), noonUtc);
    }

    @Test
    void aReminderAlreadyRecordedTodayIsNotPushedAgain() {
        User user = user("UTC");
        given(List.of(user), device(user, "ExponentPushToken[a]", null));
        when(reminderPlanner.plan(any(), any(), any())).thenReturn(List.of(THREE_DAYS));
        doReturn(Optional.empty()).when(reminderWriteService).record(any(), any(), any(), any(), any());

        job.run(NOW);

        verify(pushSender, never()).send(anyList());
    }

    @Test
    void aRaceWithAnotherRunSkipsOnlyThatReminder() {
        User user = user("UTC");
        Planned ovulation = new Planned(ReminderType.ovulation_upcoming, "Ovulation is predicted tomorrow.");
        given(List.of(user), device(user, "ExponentPushToken[a]", null));
        when(reminderPlanner.plan(any(), any(), any())).thenReturn(List.of(THREE_DAYS, ovulation));
        doThrow(new DataIntegrityViolationException("duplicate key"))
                .when(reminderWriteService).record(any(), eq(ReminderType.period_upcoming), any(), any(), any());

        job.run(NOW);

        assertThat(sentMessages()).extracting(PushMessage::body).containsExactly(ovulation.body());
    }

    @Test
    void oneUsersBadDataDoesNotCostOthersTheirReminder() {
        User broken = user("UTC");
        User fine = user("UTC");
        given(List.of(broken, fine), device(broken, "ExponentPushToken[broken]", null), device(fine, "ExponentPushToken[fine]", null));
        when(reminderPlanner.plan(eq(broken), any(), any())).thenThrow(new IllegalStateException("bad cycle data"));
        when(reminderPlanner.plan(eq(fine), any(), any())).thenReturn(List.of(THREE_DAYS));

        job.run(NOW);

        assertThat(sentMessages()).extracting(PushMessage::to).containsExactly("ExponentPushToken[fine]");
    }

    @Test
    void sendResultsAreHandedBackForTicketsAndDeadDevices() {
        User user = user("UTC");
        given(List.of(user), device(user, "ExponentPushToken[a]", null));
        when(reminderPlanner.plan(any(), any(), any())).thenReturn(List.of(THREE_DAYS));
        List<PushResult> results = List.of(new PushResult("ExponentPushToken[a]", "ticket-1", null));
        when(pushSender.send(anyList())).thenReturn(results);

        job.run(NOW);

        verify(reminderWriteService).applySendResults(results, NOW);
    }

    @Test
    void receiptsAreCollectedBeforeSendingAndEvenWhenNoOneIsDue() {
        PushTicket ticket = PushTicket.builder().ticketId("ticket-1").pushToken("ExponentPushToken[a]").createdAt(NOW.minusSeconds(1200)).build();
        Map<String, String> receipts = Map.of("ticket-1", "");
        when(reminderWriteService.ticketsDue(NOW)).thenReturn(List.of(ticket));
        when(pushSender.receipts(List.of("ticket-1"))).thenReturn(receipts);
        when(userRepository.findReminderCandidates()).thenReturn(List.of());

        job.run(NOW);

        verify(reminderWriteService).applyReceipts(List.of(ticket), receipts, NOW);
        verifyNoInteractions(deviceRepository, reminderPlanner);
    }

    @Test
    void noReceiptCallIsMadeWhenNoTicketIsDue() {
        when(userRepository.findReminderCandidates()).thenReturn(List.of());

        job.run(NOW);

        verify(pushSender, never()).receipts(anyList());
    }

    @Test
    void onlyExpoPushAddressesCount() {
        assertThat(ReminderJob.isExpoToken("ExponentPushToken[abc]")).isTrue();
        assertThat(ReminderJob.isExpoToken("ExpoPushToken[abc]")).isTrue();
        assertThat(ReminderJob.isExpoToken("3f2a-device-id")).isFalse();
        assertThat(ReminderJob.isExpoToken("")).isFalse();
        assertThat(ReminderJob.isExpoToken(null)).isFalse();
    }
}
