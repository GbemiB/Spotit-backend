package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.common.security.TokenHasher;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.partner.dto.AcceptInviteRequest;
import com.spotit.api.partner.dto.InviteRequest;
import com.spotit.api.partner.dto.PartnerSettingsRequest;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.user.entity.AccountOwner;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerWriteServiceImplTest {
    @Mock PartnerLinkRepository partnerLinkRepository;
    @Mock UserRepository userRepository;
    @Mock EmailService emailService;
    @Mock ConfigurationDomainService configurationDomainService;
    @Mock PartnerReadService partnerReadService;
    @InjectMocks PartnerWriteServiceImpl service;

    UUID ownerId = UUID.randomUUID();
    UUID partnerId = UUID.randomUUID();

    User owner() {
        return User.builder().id(ownerId).firstName("Amaka").email("amaka@example.com")
                .accountType(AccountOwner.owner).onboarded(true).build();
    }

    User newcomer() {
        return User.builder().id(partnerId).firstName("Tunde").email("tunde@example.com")
                .accountType(AccountOwner.owner).onboarded(false).build();
    }

    PartnerLink pendingInvite(String code, Instant expiresAt) {
        return PartnerLink.builder().id(UUID.randomUUID()).ownerId(ownerId).inviteEmail("tunde@example.com")
                .inviteCodeHash(TokenHasher.sha256Hex(code)).inviteExpiresAt(expiresAt)
                .status(PartnerLinkStatus.pending).partnerReminder(true).build();
    }

    PartnerLink activeLink() {
        return PartnerLink.builder().id(UUID.randomUUID()).ownerId(ownerId).partnerId(partnerId)
                .inviteEmail("tunde@example.com").inviteCodeHash("x").inviteExpiresAt(Instant.now())
                .status(PartnerLinkStatus.active).partnerReminder(true).partnerNickname("Amaka").build();
    }

    void ownerCanInvite() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner()));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.empty());
    }

    void assertError(Runnable call, ErrorCode code) {
        assertThatThrownBy(call::run).isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getErrorCode()).isEqualTo(code));
    }

    @Test
    void inviteSavesAPendingLinkAndEmailsTheCode() {
        ownerCanInvite();
        when(configurationDomainService.getAppStoreUrlAndroid()).thenReturn("https://play.example/spotit");

        service.invite(ownerId, new InviteRequest(" Tunde@Example.com "));

        ArgumentCaptor<PartnerLink> saved = ArgumentCaptor.forClass(PartnerLink.class);
        verify(partnerLinkRepository).save(saved.capture());
        PartnerLink link = saved.getValue();
        assertThat(link.getOwnerId()).isEqualTo(ownerId);
        assertThat(link.getInviteEmail()).isEqualTo("tunde@example.com");
        assertThat(link.getStatus()).isEqualTo(PartnerLinkStatus.pending);
        assertThat(link.isPartnerReminder()).isTrue();
        assertThat(Duration.between(Instant.now(), link.getInviteExpiresAt()).toHours()).isBetween(47L, 48L);

        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("tunde@example.com"), eq("Amaka invited you to Spot it"), anyString(), text.capture());
        Matcher code = Pattern.compile("Your invite code: ([A-Z2-9]{6})").matcher(text.getValue());
        assertThat(code.find()).isTrue();
        assertThat(link.getInviteCodeHash()).isEqualTo(TokenHasher.sha256Hex(code.group(1)));
        assertThat(text.getValue()).contains("48 hours").contains("https://play.example/spotit");
        verify(partnerReadService).getStatus(ownerId);
    }

    @Test
    void inviteReplacesAPendingInvite() {
        ownerCanInvite();
        PartnerLink old = pendingInvite("AAAAAA", Instant.now().plus(Duration.ofHours(10)));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending)).thenReturn(Optional.of(old));

        service.invite(ownerId, new InviteRequest("someone@example.com"));

        assertThat(old.getStatus()).isEqualTo(PartnerLinkStatus.cancelled);
    }

    @Test
    void inviteRefusesTheOwnersOwnEmail() {
        ownerCanInvite();
        assertError(() -> service.invite(ownerId, new InviteRequest("AMAKA@example.com")), ErrorCode.VALIDATION_ERROR);
        verify(partnerLinkRepository, never()).save(any());
    }

    @Test
    void inviteRequiresAnOnboardedOwner() {
        User notOnboarded = owner();
        notOnboarded.setOnboarded(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(notOnboarded));
        assertError(() -> service.invite(ownerId, new InviteRequest("tunde@example.com")), ErrorCode.PARTNER_NOT_ALLOWED);
    }

    @Test
    void aPartnerAccountCannotInvite() {
        User partner = owner();
        partner.setAccountType(AccountOwner.partner);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(partner));
        assertError(() -> service.invite(ownerId, new InviteRequest("tunde@example.com")), ErrorCode.PARTNER_NOT_ALLOWED);
    }

    @Test
    void inviteRefusedWhenAlreadySharing() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner()));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.of(activeLink()));
        assertError(() -> service.invite(ownerId, new InviteRequest("tunde@example.com")), ErrorCode.PARTNER_ALREADY_LINKED);
    }

    @Test
    void inviteRefusesAnEmailThatTracksItsOwnCycle() {
        ownerCanInvite();
        User tracker = newcomer();
        tracker.setOnboarded(true);
        when(userRepository.findByEmailIgnoreCase("tunde@example.com")).thenReturn(Optional.of(tracker));
        assertError(() -> service.invite(ownerId, new InviteRequest("tunde@example.com")), ErrorCode.PARTNER_NOT_ALLOWED);
        verify(emailService, never()).send(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void inviteRefusesAPartnerLinkedToSomeoneElse() {
        ownerCanInvite();
        User linked = newcomer();
        linked.setAccountType(AccountOwner.partner);
        when(userRepository.findByEmailIgnoreCase("tunde@example.com")).thenReturn(Optional.of(linked));
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)).thenReturn(Optional.of(activeLink()));
        assertError(() -> service.invite(ownerId, new InviteRequest("tunde@example.com")), ErrorCode.PARTNER_ALREADY_LINKED);
    }

    @Test
    void inviteAllowsAnAccountThatHasNotOnboarded() {
        ownerCanInvite();
        when(userRepository.findByEmailIgnoreCase("tunde@example.com")).thenReturn(Optional.of(newcomer()));

        service.invite(ownerId, new InviteRequest("tunde@example.com"));

        verify(partnerLinkRepository).save(any(PartnerLink.class));
    }

    @Test
    void inviteIsLimitedToFivePerDay() {
        ownerCanInvite();
        when(partnerLinkRepository.countByOwnerIdAndCreatedAtAfter(eq(ownerId), any())).thenReturn(5L);
        assertError(() -> service.invite(ownerId, new InviteRequest("tunde@example.com")), ErrorCode.INVITE_LIMIT_REACHED);
        verify(emailService, never()).send(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void aFailedEmailDoesNotFailTheInvite() {
        ownerCanInvite();
        doThrow(new MailSendException("down")).when(emailService).send(anyString(), anyString(), anyString(), anyString());

        service.invite(ownerId, new InviteRequest("tunde@example.com"));

        verify(partnerLinkRepository).save(any(PartnerLink.class));
    }

    @Test
    void resendSendsANewCodeToTheSameEmail() {
        ownerCanInvite();
        PartnerLink old = pendingInvite("AAAAAA", Instant.now().plus(Duration.ofHours(1)));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending)).thenReturn(Optional.of(old));

        service.resendInvite(ownerId);

        assertThat(old.getStatus()).isEqualTo(PartnerLinkStatus.cancelled);
        verify(emailService).send(eq("tunde@example.com"), anyString(), anyString(), anyString());
    }

    @Test
    void resendWithoutAPendingInviteIsNotFound() {
        ownerCanInvite();
        assertError(() -> service.resendInvite(ownerId), ErrorCode.NOT_FOUND);
    }

    @Test
    void cancelInviteCancelsThePendingInvite() {
        PartnerLink pending = pendingInvite("AAAAAA", Instant.now().plus(Duration.ofHours(1)));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending)).thenReturn(Optional.of(pending));

        service.cancelInvite(ownerId);

        assertThat(pending.getStatus()).isEqualTo(PartnerLinkStatus.cancelled);
    }

    void newcomerWithInvites(List<PartnerLink> invites) {
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(newcomer()));
        when(partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus("tunde@example.com", PartnerLinkStatus.pending)).thenReturn(invites);
    }

    @Test
    void acceptLinksThePartnerAndSkipsOnboarding() {
        User user = newcomer();
        PartnerLink invite = pendingInvite("K7P2QX", Instant.now().plus(Duration.ofHours(5)));
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(user));
        when(partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus("tunde@example.com", PartnerLinkStatus.pending)).thenReturn(List.of(invite));
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner()));

        service.accept(partnerId, new AcceptInviteRequest("k7p-2qx", "  Amaka  "));

        assertThat(invite.getStatus()).isEqualTo(PartnerLinkStatus.active);
        assertThat(invite.getPartnerId()).isEqualTo(partnerId);
        assertThat(invite.getPartnerNickname()).isEqualTo("Amaka");
        assertThat(invite.getAcceptedAt()).isNotNull();
        assertThat(user.getAccountType()).isEqualTo(AccountOwner.partner);
        assertThat(user.isOnboarded()).isTrue();
        verify(userRepository).save(user);
        verify(partnerReadService).getStatus(partnerId);
    }

    @Test
    void acceptWithAWrongCodeCountsTheAttempt() {
        PartnerLink invite = pendingInvite("K7P2QX", Instant.now().plus(Duration.ofHours(5)));
        newcomerWithInvites(List.of(invite));

        assertError(() -> service.accept(partnerId, new AcceptInviteRequest("ZZZZZZ", "Amaka")), ErrorCode.INVALID_CODE);

        assertThat(invite.getFailedAttempts()).isEqualTo(1);
        assertThat(invite.getStatus()).isEqualTo(PartnerLinkStatus.pending);
    }

    @Test
    void theFifthWrongCodeCancelsTheInvite() {
        PartnerLink invite = pendingInvite("K7P2QX", Instant.now().plus(Duration.ofHours(5)));
        invite.setFailedAttempts(4);
        newcomerWithInvites(List.of(invite));

        assertError(() -> service.accept(partnerId, new AcceptInviteRequest("ZZZZZZ", "Amaka")), ErrorCode.INVALID_CODE);

        assertThat(invite.getStatus()).isEqualTo(PartnerLinkStatus.cancelled);
    }

    @Test
    void acceptRefusesAnExpiredInvite() {
        newcomerWithInvites(List.of(pendingInvite("K7P2QX", Instant.now().minus(Duration.ofMinutes(1)))));
        assertError(() -> service.accept(partnerId, new AcceptInviteRequest("K7P2QX", "Amaka")), ErrorCode.INVITE_EXPIRED);
    }

    @Test
    void acceptWithoutAnInviteForTheEmailIsNotFound() {
        newcomerWithInvites(List.of());
        assertError(() -> service.accept(partnerId, new AcceptInviteRequest("K7P2QX", "Amaka")), ErrorCode.NOT_FOUND);
    }

    @Test
    void anAccountThatTracksItsOwnCycleCannotAccept() {
        User tracker = newcomer();
        tracker.setOnboarded(true);
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(tracker));
        assertError(() -> service.accept(partnerId, new AcceptInviteRequest("K7P2QX", "Amaka")), ErrorCode.PARTNER_NOT_ALLOWED);
    }

    @Test
    void acceptNeedsANickname() {
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(newcomer()));
        assertError(() -> service.accept(partnerId, new AcceptInviteRequest("K7P2QX", "   ")), ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void theOwnerHasNoSettingsToChange() {
        PartnerLink link = activeLink();
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner()));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.of(link));

        service.updateSettings(ownerId, new PartnerSettingsRequest("Ignored", false));

        assertThat(link.getPartnerNickname()).isEqualTo("Amaka");
        assertThat(link.isPartnerReminder()).isTrue();
    }

    @Test
    void thePartnerCanRenameAndTurnOffTheReminder() {
        User partner = newcomer();
        partner.setAccountType(AccountOwner.partner);
        PartnerLink link = activeLink();
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)).thenReturn(Optional.of(link));

        service.updateSettings(partnerId, new PartnerSettingsRequest(" Wifey ", false));

        assertThat(link.getPartnerNickname()).isEqualTo("Wifey");
        assertThat(link.isPartnerReminder()).isFalse();
    }

    @Test
    void eitherSideCanStopSharing() {
        PartnerLink link = activeLink();
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner()));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.of(link));

        service.unlink(ownerId);

        assertThat(link.getStatus()).isEqualTo(PartnerLinkStatus.ended);
    }

    @Test
    void deletingHerAccountEndsSharingAndCancelsHerInvite() {
        PartnerLink active = activeLink();
        PartnerLink pending = pendingInvite("K7P2QX", Instant.now().plus(Duration.ofHours(5)));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.of(active));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending)).thenReturn(Optional.of(pending));

        service.endAllFor(ownerId);

        assertThat(active.getStatus()).isEqualTo(PartnerLinkStatus.ended);
        assertThat(pending.getStatus()).isEqualTo(PartnerLinkStatus.cancelled);
    }

    @Test
    void deletingHisAccountEndsSharing() {
        PartnerLink active = activeLink();
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active)).thenReturn(Optional.of(active));

        service.endAllFor(partnerId);

        assertThat(active.getStatus()).isEqualTo(PartnerLinkStatus.ended);
    }

    @Test
    void endingWithNothingSharedChangesNothing() {
        service.endAllFor(ownerId);
        verify(partnerLinkRepository, never()).save(any());
    }

    @Test
    void stopSharingWithoutALinkIsNotFound() {
        User partner = newcomer();
        partner.setAccountType(AccountOwner.partner);
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        assertError(() -> service.unlink(partnerId), ErrorCode.NOT_FOUND);
    }
}
