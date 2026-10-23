package com.spotit.api.partner.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;
import com.spotit.api.partner.entity.PartnerCheckIn;
import com.spotit.api.partner.entity.PartnerLink;
import com.spotit.api.partner.entity.PartnerLinkStatus;
import com.spotit.api.partner.repository.PartnerCheckInRepository;
import com.spotit.api.partner.repository.PartnerLinkRepository;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.user.entity.AccountType;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerCheckInServiceImplTest {
    @Mock PartnerCheckInRepository partnerCheckInRepository;
    @Mock PartnerLinkRepository partnerLinkRepository;
    @Mock UserRepository userRepository;
    @Mock PointsWriteService pointsWriteService;
    @Mock ChallengeReadService challengeReadService;
    @Mock UserClock userClock;
    @InjectMocks PartnerCheckInServiceImpl service;

    UUID partnerId = UUID.randomUUID();
    UUID ownerId = UUID.randomUUID();
    LocalDate today = LocalDate.of(2026, 10, 24);

    User partner(AccountType type) {
        User user = User.builder().id(partnerId).firstName("Tunde").accountType(type).points(40).streak(2).build();
        when(userRepository.findById(partnerId)).thenReturn(Optional.of(user));
        return user;
    }

    void linked() {
        when(partnerLinkRepository.findFirstByPartnerIdAndStatus(partnerId, PartnerLinkStatus.active))
                .thenReturn(Optional.of(PartnerLink.builder().ownerId(ownerId).partnerId(partnerId).build()));
    }

    void assertError(Runnable call, ErrorCode code) {
        assertThatThrownBy(call::run).isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getErrorCode()).isEqualTo(code));
    }

    @Test
    void todayListsTheWaysHeCanSupportHer() {
        User user = partner(AccountType.partner);
        when(userClock.today(user)).thenReturn(today);
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        PartnerCheckInResponse response = service.getToday(partnerId);

        assertThat(response.checkedInToday()).isFalse();
        assertThat(response.options()).extracting("label").contains("Checked in on her", "Brought her snacks", "Listened to her");
        assertThat(response.reward()).isEqualTo(10);
        assertThat(response.streak()).isEqualTo(2);
    }

    @Test
    void todayShowsWhatHeAlreadyChose() {
        User user = partner(AccountType.partner);
        when(userClock.today(user)).thenReturn(today);
        when(partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today))
                .thenReturn(Optional.of(PartnerCheckIn.builder().supports("2,6").build()));

        PartnerCheckInResponse response = service.getToday(partnerId);

        assertThat(response.checkedInToday()).isTrue();
        assertThat(response.supports()).containsExactly("snacks", "listened");
    }

    @Test
    void checkingInSavesItAndAwardsHisPoints() {
        User user = partner(AccountType.partner);
        linked();
        when(userClock.today(user)).thenReturn(today);
        when(pointsWriteService.recordCheckIn(partnerId)).thenReturn(new PointsWriteService.LogPointsResult(10, 50, 3, true));

        PartnerCheckInResponse response = service.checkIn(partnerId, new PartnerCheckInRequest(List.of("chores", "snacks", "chores")));

        ArgumentCaptor<PartnerCheckIn> saved = ArgumentCaptor.forClass(PartnerCheckIn.class);
        verify(partnerCheckInRepository).save(saved.capture());
        assertThat(saved.getValue().getSupports()).isEqualTo("4,2");
        assertThat(saved.getValue().getOwnerId()).isEqualTo(ownerId);
        assertThat(saved.getValue().getCheckInDate()).isEqualTo(today);
        assertThat(response.pointsAwarded()).isEqualTo(10);
        assertThat(response.newBalance()).isEqualTo(50);
        assertThat(response.streak()).isEqualTo(3);
        assertThat(response.supports()).containsExactly("chores", "snacks");
    }

    @Test
    void onlyOneCheckInADay() {
        User user = partner(AccountType.partner);
        linked();
        when(userClock.today(user)).thenReturn(today);
        when(partnerCheckInRepository.findByPartnerIdAndCheckInDate(partnerId, today)).thenReturn(Optional.of(PartnerCheckIn.builder().build()));

        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of("snacks"))), ErrorCode.ALREADY_CLAIMED);
        verify(pointsWriteService, never()).recordCheckIn(any());
    }

    @Test
    void anUnknownWayIsRefused() {
        partner(AccountType.partner);
        linked();
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of("flowers"))), ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void checkingInNeedsAnActiveLink() {
        partner(AccountType.partner);
        assertError(() -> service.checkIn(partnerId, new PartnerCheckInRequest(List.of("snacks"))), ErrorCode.NOT_FOUND);
    }

    @Test
    void anOwnerCannotUsePartnerCheckIns() {
        partner(AccountType.owner);
        assertError(() -> service.getToday(partnerId), ErrorCode.NOT_FOUND);
    }
}
