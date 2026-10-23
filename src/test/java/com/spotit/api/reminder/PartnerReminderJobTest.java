package com.spotit.api.reminder;

import com.spotit.api.device.dto.RegisterDeviceRequest;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.device.service.DeviceWriteService;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.reminder.push.PushMessage;
import com.spotit.api.reminder.push.PushResult;
import com.spotit.api.reminder.push.PushSender;
import com.spotit.api.reminder.repository.ReminderDeliveryRepository;
import com.spotit.api.user.entity.AccountType;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PartnerReminderJobTest {
    static final Instant LAGOS_0600_FIVE_DAYS_BEFORE = Instant.parse("2026-10-28T05:00:00Z");
    static final String PARTNER_TOKEN = "ExponentPushToken[partner]";

    @Autowired ReminderJob reminderJob;
    @Autowired UserRepository userRepository;
    @Autowired DeviceRepository deviceRepository;
    @Autowired DeviceWriteService deviceWriteService;
    @Autowired PartnerLinkRepository partnerLinkRepository;
    @Autowired ReminderDeliveryRepository reminderDeliveryRepository;
    @MockBean PushSender pushSender;

    UUID ownerId;
    UUID partnerId;

    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();
        reminderDeliveryRepository.deleteAll();
        partnerLinkRepository.deleteAll();
        when(pushSender.send(anyList())).thenAnswer(inv -> inv.<List<PushMessage>>getArgument(0).stream()
                .map(message -> new PushResult(message.to(), "ticket-" + UUID.randomUUID(), null)).toList());
        ownerId = save("Amaka", AccountType.owner, LocalDate.of(2026, 10, 5));
        partnerId = save("Tunde", AccountType.partner, LocalDate.of(2026, 10, 2));
        deviceWriteService.register(partnerId, new RegisterDeviceRequest(PARTNER_TOKEN, "android", "Africa/Lagos"));
    }

    @AfterEach
    void tearDown() {
        partnerLinkRepository.deleteAll();
        deviceRepository.deleteAll();
    }

    UUID save(String name, AccountType type, LocalDate lastPeriod) {
        return userRepository.save(User.builder().firstName(name).lastName("Test")
                .email(name.toLowerCase() + "-" + UUID.randomUUID() + "@example.com").passwordHash("x").emailVerified(true)
                .onboarded(true).accountType(type).cycleLength(28).periodLength(5).lastPeriodDate(lastPeriod)
                .themePref(ThemePref.dark).notifPeriod(true).notifOvulation(true).notifDailyLog(true).notifDigest(false).build()).getId();
    }

    void link(boolean reminder, PartnerLinkStatus status) {
        partnerLinkRepository.save(PartnerLink.builder().ownerId(ownerId).partnerId(partnerId).inviteEmail("tunde@example.com")
                .inviteCodeHash("x").inviteExpiresAt(Instant.now()).status(status)
                .partnerReminder(reminder).partnerNickname("Amaka").build());
    }

    List<PushMessage> sentToPartner() {
        List<PushMessage> all = new ArrayList<>();
        mockingDetails(pushSender).getInvocations().stream()
                .filter(invocation -> invocation.getMethod().getName().equals("send"))
                .forEach(invocation -> all.addAll(invocation.<List<PushMessage>>getArgument(0)));
        return all.stream().filter(message -> message.to().equals(PARTNER_TOKEN)).toList();
    }

    @Test
    void thePartnerGetsOneReminderFiveDaysBeforeAtSixInHisTimeZone() {
        link(true, PartnerLinkStatus.active);

        reminderJob.run(LAGOS_0600_FIVE_DAYS_BEFORE.minusSeconds(60));
        assertThat(sentToPartner()).isEmpty();

        reminderJob.run(LAGOS_0600_FIVE_DAYS_BEFORE);
        reminderJob.run(LAGOS_0600_FIVE_DAYS_BEFORE.plusSeconds(900));

        List<PushMessage> sent = sentToPartner();
        assertThat(sent).hasSize(1);
        assertThat(sent.get(0).title()).isEqualTo("Period reminder");
        assertThat(sent.get(0).body()).isEqualTo("Amaka's period is expected to start in 5 days.");
        assertThat(sent.get(0).data()).containsEntry("screen", "partner").containsEntry("type", "partner_period_upcoming");
        assertThat(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active).orElseThrow()
                .getLastRemindedPeriodDate()).isEqualTo(LocalDate.of(2026, 11, 2));
        assertThat(reminderDeliveryRepository.findAll()).filteredOn(d -> d.getUserId().equals(partnerId)).hasSize(1);
    }

    @Test
    void noReminderWhenHeTurnedItOff() {
        link(false, PartnerLinkStatus.active);
        reminderJob.run(LAGOS_0600_FIVE_DAYS_BEFORE);
        assertThat(sentToPartner()).isEmpty();
    }

    @Test
    void noReminderAfterSharingEnded() {
        link(true, PartnerLinkStatus.ended);
        reminderJob.run(LAGOS_0600_FIVE_DAYS_BEFORE);
        assertThat(sentToPartner()).isEmpty();
    }
}
