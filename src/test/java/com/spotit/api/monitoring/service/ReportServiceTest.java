package com.spotit.api.monitoring.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.device.entity.Device;
import com.spotit.api.device.entity.DevicePlatform;
import com.spotit.api.device.repository.DeviceRepository;
import com.spotit.api.monitoring.entity.AppCrash;
import com.spotit.api.monitoring.entity.FailedAction;
import com.spotit.api.monitoring.repository.AppCrashRepository;
import com.spotit.api.monitoring.repository.FailedActionRepository;
import com.spotit.api.partner.repository.PartnerCheckInRepository;
import com.spotit.api.user.entity.AccountOwner;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @Mock UserRepository userRepository;
    @Mock DeviceRepository deviceRepository;
    @Mock AppCrashRepository appCrashRepository;
    @Mock FailedActionRepository failedActionRepository;
    @Mock PartnerCheckInRepository partnerCheckInRepository;
    @InjectMocks ReportService service;

    LocalDate from = LocalDate.of(2026, 10, 1);
    LocalDate to = LocalDate.of(2026, 10, 31);

    @Test
    void theSummaryCountsDownloadsActiveUsersCrashesAndFailures() {
        User her = User.builder().id(UUID.randomUUID()).firstName("Amaka").email("a@x.com").accountType(AccountOwner.owner)
                .lastActiveAt(Instant.parse("2026-10-30T10:00:00Z")).lastLogDate(LocalDate.of(2026, 10, 29)).build();
        User him = User.builder().id(UUID.randomUUID()).firstName("Tunde").email("t@x.com").accountType(AccountOwner.partner).build();
        when(userRepository.findFirstOnboardedBetween(any(), any())).thenReturn(List.of(her, him));
        when(deviceRepository.findByUserIdIn(anyList())).thenReturn(List.of(
                Device.builder().userId(her.getId()).platform(DevicePlatform.android).build()));
        when(userRepository.countByCreatedAtBetween(any(), any())).thenReturn(3L);
        when(userRepository.countByLastActiveAtAfter(any())).thenReturn(1L, 2L, 2L);
        when(appCrashRepository.findByHappenedAtBetween(any(), any())).thenReturn(List.of(
                AppCrash.builder().message("TypeError: x is undefined").appVersion("1.1.0").happenedAt(Instant.parse("2026-10-02T00:00:00Z")).build(),
                AppCrash.builder().message("TypeError: x is undefined").appVersion("1.1.1").happenedAt(Instant.parse("2026-10-05T00:00:00Z")).build(),
                AppCrash.builder().message("Network failed").appVersion("1.1.1").happenedAt(Instant.parse("2026-10-03T00:00:00Z")).build()));
        when(failedActionRepository.findByHappenedAtBetween(any(), any())).thenReturn(List.of(
                FailedAction.builder().action("POST /api/v1/logs/period").status(500).build(),
                FailedAction.builder().action("POST /api/v1/logs/period").status(500).build(),
                FailedAction.builder().action("GET /api/v1/insights").status(503).build()));
        when(userRepository.findByLastActive(any())).thenReturn(new PageImpl<>(List.of(her, him), PageRequest.of(0, 50), 2));
        Instant checkIn = Instant.parse("2026-10-28T08:00:00Z");
        when(partnerCheckInRepository.findLatestByPartnerIds(List.of(him.getId())))
                .thenReturn(List.<Object[]>of(new Object[] {him.getId(), checkIn}));

        var summary = service.summary(from, to, 0, 50);

        assertThat(summary.downloads().total()).isEqualTo(2);
        assertThat(summary.downloads().owners()).isEqualTo(1);
        assertThat(summary.downloads().partners()).isEqualTo(1);
        assertThat(summary.downloads().byPlatform()).containsEntry("android", 1L).containsEntry("unknown", 1L).containsEntry("ios", 0L);
        assertThat(summary.signups()).isEqualTo(3);
        assertThat(summary.active().daily()).isEqualTo(1);
        assertThat(summary.active().monthly()).isEqualTo(2);
        assertThat(summary.crashes().total()).isEqualTo(3);
        assertThat(summary.crashes().top().get(0).message()).isEqualTo("TypeError: x is undefined");
        assertThat(summary.crashes().top().get(0).count()).isEqualTo(2);
        assertThat(summary.crashes().top().get(0).latestAppVersion()).isEqualTo("1.1.1");
        assertThat(summary.failedActions().total()).isEqualTo(3);
        assertThat(summary.failedActions().byAction()).containsEntry("POST /api/v1/logs/period", 2L);
        assertThat(summary.lastActivity().users()).hasSize(2);
        assertThat(summary.lastActivity().users().get(1).lastPartnerCheckInAt()).isEqualTo(checkIn);
        assertThat(summary.lastActivity().totalUsers()).isEqualTo(2);
    }

    @Test
    void anEmptyRangeStillAnswers() {
        when(userRepository.findFirstOnboardedBetween(any(), any())).thenReturn(List.of());
        when(appCrashRepository.findByHappenedAtBetween(any(), any())).thenReturn(List.of());
        when(failedActionRepository.findByHappenedAtBetween(any(), any())).thenReturn(List.of());
        when(userRepository.findByLastActive(any())).thenReturn(new PageImpl<>(List.of()));

        var summary = service.summary(from, to, -1, 999);

        assertThat(summary.downloads().total()).isZero();
        assertThat(summary.lastActivity().size()).isEqualTo(200);
        assertThat(summary.lastActivity().page()).isZero();
    }

    @Test
    void theCrashListIsPaged() {
        AppCrash crash = AppCrash.builder().id(UUID.randomUUID()).message("Boom").screen("Home").happenedAt(Instant.now()).build();
        when(appCrashRepository.findByHappenedAtBetweenOrderByHappenedAtDesc(any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(crash), PageRequest.of(0, 50), 1));

        var page = service.crashes(from, to, 0, 0);

        assertThat(page.size()).isEqualTo(50);
        assertThat(page.total()).isEqualTo(1);
        assertThat(page.crashes().get(0).screen()).isEqualTo("Home");
    }

    @Test
    void aBackwardsOrTooLongRangeIsRefused() {
        assertThatThrownBy(() -> service.summary(to, from, 0, 50)).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.crashes(from, from.plusDays(400), 0, 50)).isInstanceOf(ApiException.class);
    }
}
