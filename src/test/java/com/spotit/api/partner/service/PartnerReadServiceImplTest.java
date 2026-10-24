package com.spotit.api.partner.service;

import com.spotit.api.partner.dto.PartnerStatusResponse;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.user.entity.AccountOwner;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerReadServiceImplTest {
    @Mock PartnerLinkRepository partnerLinkRepository;
    @Mock UserRepository userRepository;
    @InjectMocks PartnerReadServiceImpl service;

    UUID ownerId = UUID.randomUUID();
    UUID partnerId = UUID.randomUUID();

    User user(UUID id, String name, AccountOwner type, boolean onboarded) {
        User user = User.builder().id(id).firstName(name).email(name.toLowerCase() + "@example.com")
                .accountType(type).onboarded(onboarded).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        return user;
    }

    PartnerLink link(PartnerLinkStatus status, Instant expiresAt) {
        return PartnerLink.builder().ownerId(ownerId).partnerId(partnerId).inviteEmail("tunde@example.com")
                .inviteExpiresAt(expiresAt).status(status).partnerReminder(true)
                .partnerNickname("Amaka").createdAt(Instant.now()).build();
    }

    @Test
    void anOwnerWithNoInviteHasStateNone() {
        user(ownerId, "Amaka", AccountOwner.owner, true);
        PartnerStatusResponse status = service.getStatus(ownerId);
        assertThat(status.role()).isEqualTo("owner");
        assertThat(status.state()).isEqualTo("none");
    }

    @Test
    void anOwnerWithAPendingInviteSeesTheEmailAndExpiry() {
        user(ownerId, "Amaka", AccountOwner.owner, true);
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.empty());
        Instant expires = Instant.now().plus(Duration.ofHours(30));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending))
                .thenReturn(Optional.of(link(PartnerLinkStatus.pending, expires)));

        PartnerStatusResponse status = service.getStatus(ownerId);

        assertThat(status.state()).isEqualTo("pending");
        assertThat(status.inviteEmail()).isEqualTo("tunde@example.com");
        assertThat(status.inviteExpiresAt()).isEqualTo(expires);
    }

    @Test
    void anOldInviteShowsAsExpired() {
        user(ownerId, "Amaka", AccountOwner.owner, true);
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.empty());
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.pending))
                .thenReturn(Optional.of(link(PartnerLinkStatus.pending, Instant.now().minus(Duration.ofHours(1)))));

        assertThat(service.getStatus(ownerId).state()).isEqualTo("expired");
    }

    @Test
    void aLinkedOwnerSeesThePartnerAndTheSharingSetting() {
        user(ownerId, "Amaka", AccountOwner.owner, true);
        user(partnerId, "Tunde", AccountOwner.partner, true);
        Instant accepted = Instant.parse("2026-10-23T09:00:00Z");
        PartnerLink linked = link(PartnerLinkStatus.active, Instant.now());
        linked.setAcceptedAt(accepted);
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, PartnerLinkStatus.active)).thenReturn(Optional.of(linked));

        PartnerStatusResponse status = service.getStatus(ownerId);

        assertThat(status.state()).isEqualTo("active");
        assertThat(status.partnerFirstName()).isEqualTo("Tunde");
        assertThat(status.sharedSince()).isEqualTo(accepted);
    }

    @Test
    void aNewAccountWithAnInviteSeesWhoInvitedThem() {
        user(partnerId, "Tunde", AccountOwner.owner, false);
        user(ownerId, "Amaka", AccountOwner.owner, true);
        when(partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus("tunde@example.com", PartnerLinkStatus.pending))
                .thenReturn(List.of(link(PartnerLinkStatus.pending, Instant.now().plus(Duration.ofHours(2)))));
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(partnerId, PartnerLinkStatus.active)).thenReturn(Optional.empty());
        when(partnerLinkRepository.findFirstByOwnerIdAndStatusOrderByCreatedAtDesc(partnerId, PartnerLinkStatus.pending)).thenReturn(Optional.empty());

        PartnerStatusResponse status = service.getStatus(partnerId);

        assertThat(status.state()).isEqualTo("invited");
        assertThat(status.ownerFirstName()).isEqualTo("Amaka");
    }

    @Test
    void anExpiredInviteIsNotShownToTheNewAccount() {
        user(partnerId, "Tunde", AccountOwner.owner, false);
        when(partnerLinkRepository.findByInviteEmailIgnoreCaseAndStatus("tunde@example.com", PartnerLinkStatus.pending))
                .thenReturn(List.of(link(PartnerLinkStatus.pending, Instant.now().minus(Duration.ofHours(2)))));

        assertThat(service.getStatus(partnerId).state()).isEqualTo("none");
    }

    @Test
    void aLinkedPartnerSeesTheOwnerAndTheirSettings() {
        user(partnerId, "Tunde", AccountOwner.partner, true);
        user(ownerId, "Amaka", AccountOwner.owner, true);
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active))
                .thenReturn(Optional.of(link(PartnerLinkStatus.active, Instant.now())));

        PartnerStatusResponse status = service.getStatus(partnerId);

        assertThat(status.role()).isEqualTo("partner");
        assertThat(status.state()).isEqualTo("active");
        assertThat(status.ownerFirstName()).isEqualTo("Amaka");
        assertThat(status.nickname()).isEqualTo("Amaka");
        assertThat(status.partnerReminder()).isTrue();
    }

    @Test
    void aPartnerWhoseSharingStoppedSeesEnded() {
        user(partnerId, "Tunde", AccountOwner.partner, true);
        PartnerStatusResponse status = service.getStatus(partnerId);
        assertThat(status.role()).isEqualTo("partner");
        assertThat(status.state()).isEqualTo("ended");
    }

    @Test
    void aLinkedPartnerGetsHerNameAndCycleSettings() {
        User owner = user(ownerId, "Amaka", AccountOwner.owner, true);
        owner.setCycleLength(29);
        owner.setPeriodLength(4);
        owner.setLastPeriodDate(java.time.LocalDate.of(2026, 10, 6));
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active))
                .thenReturn(Optional.of(link(PartnerLinkStatus.active, Instant.now())));

        var profile = service.getOwnerProfile(partnerId);

        assertThat(profile.firstName()).isEqualTo("Amaka");
        assertThat(profile.nickname()).isEqualTo("Amaka");
        assertThat(profile.cycleLength()).isEqualTo(29);
        assertThat(profile.periodLength()).isEqualTo(4);
        assertThat(profile.lastPeriodDate()).isEqualTo(java.time.LocalDate.of(2026, 10, 6));
        assertThat(profile.trackingStartDate()).isEqualTo(java.time.LocalDate.of(2026, 10, 1));
    }

    @Test
    void withoutAnActiveLinkThereIsNoProfile() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.getOwnerProfile(partnerId))
                .isInstanceOf(com.spotit.api.common.exception.ApiException.class);
    }
}
