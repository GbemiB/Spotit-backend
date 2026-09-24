package com.spotit.api.user.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.mail.EmailService;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import com.spotit.api.user.dto.ExportDataResponse;
import com.spotit.api.user.dto.ExportJobResponse;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.dto.OnboardingRequest;
import com.spotit.api.user.dto.OnboardingResponse;
import com.spotit.api.user.dto.ResetResponse;
import com.spotit.api.user.dto.UpdateNotificationPrefsRequest;
import com.spotit.api.user.dto.UpdateProfileRequest;
import com.spotit.api.user.dto.UserResponse;
import com.spotit.api.user.entity.ExportJob;
import com.spotit.api.user.entity.Goal;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.ExportJobRepository;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWriteServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock ExportJobRepository exportJobRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @Mock PointsHistoryRepository pointsHistoryRepository;
    @Mock UserBadgeRepository userBadgeRepository;
    @Mock UserChallengeProgressRepository userChallengeProgressRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @Mock UserReadService userReadService;
    @Mock EmailService emailService;

    UserWriteServiceImpl service;
    UUID userId = UUID.randomUUID();
    User user;

    @BeforeEach
    void setUp() {
        service = new UserWriteServiceImpl(userRepository, exportJobRepository, cycleLogRepository, pointsHistoryRepository,
                userBadgeRepository, userChallengeProgressRepository, configurationDomainService, userReadService, emailService);
        user = User.builder().id(userId).firstName("Jane").lastName("Doe").email("jane@example.com").goal(Goal.track)
                .cycleLength(28).periodLength(5).themePref(ThemePref.system).onboarded(true).points(620).streak(4).longestStreak(9)
                .notifPeriod(true).notifOvulation(true).notifDailyLog(true).notifDigest(false).build();
        lenient().when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        lenient().when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void updateProfileAppliesOnlyTheSentFields() {
        UserResponse response = service.updateProfile(userId, new UpdateProfileRequest("Janet", null, 30, null, "dark"));

        assertThat(response.firstName()).isEqualTo("Janet");
        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.cycleLength()).isEqualTo(30);
        assertThat(response.periodLength()).isEqualTo(5);
        assertThat(response.themePref()).isEqualTo("dark");
    }

    @Test
    void updateProfileIgnoresBlankNames() {
        service.updateProfile(userId, new UpdateProfileRequest(" ", "", null, 6, null));

        assertThat(user.getFirstName()).isEqualTo("Jane");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPeriodLength()).isEqualTo(6);
    }

    @Test
    void updateProfileRejectsAnOutOfRangeCycleLength() {
        assertThatThrownBy(() -> service.updateProfile(userId, new UpdateProfileRequest(null, null, 20, null, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.CYCLE_LENGTH_OUT_OF_RANGE);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfileRejectsAnOutOfRangePeriodLength() {
        assertThatThrownBy(() -> service.updateProfile(userId, new UpdateProfileRequest(null, null, null, 11, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.PERIOD_LENGTH_OUT_OF_RANGE);
    }

    @Test
    void updateProfileForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProfile(userId, new UpdateProfileRequest(null, null, null, null, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void updateNotificationPrefsAppliesOnlyTheSentFlags() {
        NotificationPrefsResponse response = service.updateNotificationPrefs(userId, new UpdateNotificationPrefsRequest(false, null, null, true));

        assertThat(response).isEqualTo(new NotificationPrefsResponse(false, true, true, true));
        verify(userRepository).save(user);
    }

    @Test
    void updateNotificationPrefsCanChangeEveryFlag() {
        assertThat(service.updateNotificationPrefs(userId, new UpdateNotificationPrefsRequest(false, false, false, false)))
                .isEqualTo(new NotificationPrefsResponse(false, false, false, false));
    }

    @Test
    void completeOnboardingStoresTheAnswers() {
        LocalDate dob = LocalDate.of(1998, 4, 12);
        LocalDate lastPeriod = LocalDate.of(2026, 9, 1);

        OnboardingResponse response = service.completeOnboarding(userId, new OnboardingRequest(dob, lastPeriod, "conceive", 30, 6));

        assertThat(response).isEqualTo(new OnboardingResponse(true, 30, 6, "conceive"));
        assertThat(user.getDob()).isEqualTo(dob);
        assertThat(user.getLastPeriodDate()).isEqualTo(lastPeriod);
        assertThat(user.isOnboarded()).isTrue();
    }

    @Test
    void completeOnboardingKeepsDefaultLengthsWhenNotSent() {
        OnboardingResponse response = service.completeOnboarding(userId, new OnboardingRequest(LocalDate.of(1998, 4, 12), null, "track", null, null));

        assertThat(response.cycleLength()).isEqualTo(28);
        assertThat(response.periodLength()).isEqualTo(5);
    }

    @Test
    void completeOnboardingRejectsAnUnknownGoal() {
        assertThatThrownBy(() -> service.completeOnboarding(userId, new OnboardingRequest(LocalDate.of(1998, 4, 12), null, "win", null, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.INVALID_GOAL);
    }

    @Test
    void completeOnboardingRejectsOutOfRangeLengths() {
        LocalDate dob = LocalDate.of(1998, 4, 12);
        assertThatThrownBy(() -> service.completeOnboarding(userId, new OnboardingRequest(dob, null, "track", 50, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.CYCLE_LENGTH_OUT_OF_RANGE);
        assertThatThrownBy(() -> service.completeOnboarding(userId, new OnboardingRequest(dob, null, "track", null, 1)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.PERIOD_LENGTH_OUT_OF_RANGE);
    }

    private UUID stubExport() {
        UUID jobId = UUID.randomUUID();
        when(exportJobRepository.save(any(ExportJob.class))).thenAnswer(inv -> {
            ExportJob job = inv.getArgument(0);
            job.setId(jobId);
            return job;
        });
        UserResponse profile = new UserResponse(userId, "Jane", "Doe", "jane@example.com", null, "track", 28, 5, null, "system", true, false);
        when(userReadService.getExportData(userId, jobId)).thenReturn(new ExportDataResponse(profile,
                List.of(Map.of("date", "2026-09-01", "flow", "heavy", "mood", "", "symptoms", List.of("cramps"), "notes", "")),
                List.of(Map.of("label", "Daily log", "delta", 10, "date", "2026-09-01"))));
        return jobId;
    }

    @Test
    void requestExportEmailsTheCsvAndReturnsAReadyJob() {
        UUID jobId = stubExport();

        ExportJobResponse response = service.requestExport(userId);

        assertThat(response).isEqualTo(new ExportJobResponse(jobId, "ready"));
        ArgumentCaptor<byte[]> csv = ArgumentCaptor.forClass(byte[].class);
        verify(emailService).sendWithAttachment(eq("jane@example.com"), anyString(), contains("Jane"), anyString(),
                eq("spotit-export.csv"), csv.capture(), eq("text/csv"));
        assertThat(new String(csv.getValue(), StandardCharsets.UTF_8)).contains("jane@example.com").contains("2026-09-01");
    }

    @Test
    void requestExportGreetsUsersWithoutAFirstNameGenerically() {
        user.setFirstName(null);
        stubExport();

        service.requestExport(userId);

        verify(emailService).sendWithAttachment(anyString(), anyString(), contains("there"), anyString(), anyString(), any(), anyString());
    }

    @Test
    void requestExportStillSucceedsWhenTheEmailFails() {
        UUID jobId = stubExport();
        doThrow(new MailSendException("smtp down")).when(emailService)
                .sendWithAttachment(anyString(), anyString(), anyString(), anyString(), anyString(), any(), anyString());

        assertThat(service.requestExport(userId).jobId()).isEqualTo(jobId);
    }

    @Test
    void resetWipesActivityAndRestoresDefaults() {
        when(configurationDomainService.getCycleDefaultLength()).thenReturn(28);
        when(configurationDomainService.getCycleDefaultPeriodLength()).thenReturn(5);
        user.setThemePref(ThemePref.dark);
        user.setNotifDigest(true);

        ResetResponse response = service.resetAllData(userId);

        verify(cycleLogRepository).deleteByUserId(userId);
        verify(pointsHistoryRepository).deleteByUserId(userId);
        verify(userBadgeRepository).deleteByUserId(userId);
        verify(userChallengeProgressRepository).deleteByUserId(userId);
        assertThat(user.isOnboarded()).isFalse();
        assertThat(user.getGoal()).isNull();
        assertThat(user.getPoints()).isZero();
        assertThat(user.getStreak()).isZero();
        assertThat(user.getLongestStreak()).isZero();
        assertThat(user.getThemePref()).isEqualTo(ThemePref.system);
        assertThat(user.isNotifDigest()).isFalse();
        assertThat(user.getCycleLength()).isEqualTo(28);
        assertThat(response.message()).isEqualTo("All data has been reset.");
        assertThat(response.resetAt()).isNotNull();
    }
}
