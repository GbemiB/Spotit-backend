package com.spotit.api.partner;

import com.spotit.api.auth.service.AuthWriteService;
import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.partner.dto.AcceptInviteRequest;
import com.spotit.api.partner.dto.InviteRequest;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerSettingsRequest;
import com.spotit.api.partner.service.PartnerCheckInService;
import com.spotit.api.partner.service.PartnerReadService;
import com.spotit.api.partner.service.PartnerWriteService;
import com.spotit.api.user.entity.AccountType;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class PartnerFlowTest {
    @Autowired PartnerWriteService partnerWriteService;
    @Autowired PartnerReadService partnerReadService;
    @Autowired PartnerCheckInService partnerCheckInService;
    @Autowired UserRepository userRepository;
    @Autowired AuthWriteService authWriteService;
    @MockBean EmailService emailService;

    User save(String name, boolean onboarded) {
        return userRepository.save(User.builder().firstName(name).lastName("Test")
                .email(name.toLowerCase() + "-" + UUID.randomUUID() + "@example.com").passwordHash("x").emailVerified(true)
                .onboarded(onboarded).cycleLength(28).periodLength(5).lastPeriodDate(LocalDate.now().minusDays(10))
                .themePref(ThemePref.dark).build());
    }

    @Test
    void sheInvitesHeAcceptsAndTheyCanChangeSettingsAndStopSharing() {
        User owner = save("Amaka", true);
        User partner = save("Tunde", false);

        partnerWriteService.invite(owner.getId(), new InviteRequest(partner.getEmail()));
        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq(partner.getEmail()), anyString(), anyString(), text.capture());
        Matcher code = Pattern.compile("Your invite code: ([A-Z2-9]{6})").matcher(text.getValue());
        assertThat(code.find()).isTrue();
        assertThat(partnerReadService.getStatus(owner.getId()).state()).isEqualTo("pending");
        assertThat(partnerReadService.getStatus(partner.getId()).state()).isEqualTo("invited");

        partnerWriteService.accept(partner.getId(), new AcceptInviteRequest(code.group(1).toLowerCase(), "Amaka"));

        User linked = userRepository.findById(partner.getId()).orElseThrow();
        assertThat(linked.getAccountType()).isEqualTo(AccountType.partner);
        assertThat(linked.isOnboarded()).isTrue();
        assertThat(partnerReadService.getStatus(owner.getId()).partnerFirstName()).isEqualTo("Tunde");
        assertThat(partnerReadService.getStatus(partner.getId()).nickname()).isEqualTo("Amaka");

        assertThat(partnerReadService.getOwnerProfile(partner.getId()).firstName()).isEqualTo("Amaka");
        partnerWriteService.updateSettings(partner.getId(), new PartnerSettingsRequest("Babe", false));
        assertThat(partnerReadService.getStatus(partner.getId()).nickname()).isEqualTo("Babe");
        assertThat(partnerReadService.getStatus(partner.getId()).partnerReminder()).isFalse();

        var checkIn = partnerCheckInService.checkIn(partner.getId(), new PartnerCheckInRequest(java.util.List.of("checked_in", "snacks")));
        assertThat(checkIn.checkedInToday()).isTrue();
        assertThat(checkIn.pointsAwarded()).isPositive();
        assertThat(checkIn.streak()).isEqualTo(1);
        assertThat(partnerCheckInService.getToday(partner.getId()).supports()).containsExactly("checked_in", "snacks");
        assertThatThrownBy(() -> partnerCheckInService.checkIn(partner.getId(), new PartnerCheckInRequest(java.util.List.of("listened"))))
                .isInstanceOf(ApiException.class);

        partnerWriteService.unlink(partner.getId());
        assertThat(partnerReadService.getStatus(partner.getId()).state()).isEqualTo("ended");
        assertThat(partnerReadService.getStatus(owner.getId()).state()).isEqualTo("none");
    }

    @Test
    void whenSheDeletesHerAccountHeLosesAccessStraightAway() {
        User owner = save("Ngozi", true);
        User partner = save("Emeka", false);
        partnerWriteService.invite(owner.getId(), new InviteRequest(partner.getEmail()));
        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq(partner.getEmail()), anyString(), anyString(), text.capture());
        Matcher code = Pattern.compile("Your invite code: ([A-Z2-9]{6})").matcher(text.getValue());
        assertThat(code.find()).isTrue();
        partnerWriteService.accept(partner.getId(), new AcceptInviteRequest(code.group(1), "Ngozi"));

        authWriteService.scheduleAccountDeletion(owner.getId());

        assertThat(partnerReadService.getStatus(partner.getId()).state()).isEqualTo("ended");
        assertThatThrownBy(() -> partnerReadService.getOwnerProfile(partner.getId())).isInstanceOf(ApiException.class);
        assertThatThrownBy(() -> partnerCheckInService.checkIn(partner.getId(), new PartnerCheckInRequest(java.util.List.of("listened"))))
                .isInstanceOf(ApiException.class);
    }
}
