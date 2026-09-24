package com.spotit.api.user.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.entity.PointsHistoryEntry;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.user.dto.ExportDataResponse;
import com.spotit.api.user.dto.NotificationPrefsResponse;
import com.spotit.api.user.dto.UserResponse;
import com.spotit.api.user.entity.ExportJob;
import com.spotit.api.user.entity.Goal;
import com.spotit.api.user.entity.ThemePref;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.ExportJobRepository;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReadServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock ExportJobRepository exportJobRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @Mock PointsHistoryRepository pointsHistoryRepository;
    @InjectMocks UserReadServiceImpl service;

    UUID userId = UUID.randomUUID();
    UUID jobId = UUID.randomUUID();

    private User user() {
        return User.builder().id(userId).firstName("Jane").lastName("Doe").email("jane@example.com").dob(LocalDate.of(1998, 4, 12))
                .goal(Goal.track).cycleLength(28).periodLength(5).themePref(ThemePref.dark).onboarded(true)
                .notifPeriod(true).notifOvulation(false).notifDailyLog(true).notifDigest(false).build();
    }

    @Test
    void profileMapsTheUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user()));

        UserResponse profile = service.getProfile(userId);

        assertThat(profile.email()).isEqualTo("jane@example.com");
        assertThat(profile.goal()).isEqualTo("track");
        assertThat(profile.themePref()).isEqualTo("dark");
        assertThat(profile.onboarded()).isTrue();
    }

    @Test
    void profileWithoutAGoalHasANullGoal() {
        User user = user();
        user.setGoal(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThat(service.getProfile(userId).goal()).isNull();
    }

    @Test
    void profileForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProfile(userId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void notificationPrefsMirrorTheUserFlags() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user()));

        assertThat(service.getNotificationPrefs(userId)).isEqualTo(new NotificationPrefsResponse(true, false, true, false));
    }

    @Test
    void exportBundlesProfileLogsAndPointsHistory() {
        when(exportJobRepository.findByIdAndUserId(jobId, userId)).thenReturn(Optional.of(ExportJob.builder().id(jobId).userId(userId).build()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user()));
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(eq(userId), any(), any())).thenReturn(List.of(
                CycleLog.builder().logDate(LocalDate.of(2026, 9, 1)).flow(FlowIntensity.heavy).symptoms(List.of(1)).notes("ouch").build(),
                CycleLog.builder().logDate(LocalDate.of(2026, 9, 2)).build()));
        when(pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged())).thenReturn(List.of(
                PointsHistoryEntry.builder().label("Daily log").delta(10).occurredOn(LocalDate.of(2026, 9, 1)).build()));

        ExportDataResponse export = service.getExportData(userId, jobId);

        assertThat(export.profile().firstName()).isEqualTo("Jane");
        assertThat(export.logs()).hasSize(2);
        assertThat(export.logs().get(0)).containsEntry("flow", "heavy").containsEntry("symptoms", List.of("cramps")).containsEntry("notes", "ouch");
        assertThat(export.logs().get(1)).containsEntry("flow", "").containsEntry("mood", "").containsEntry("notes", "");
        assertThat(export.pointsHistory()).singleElement().satisfies(h -> assertThat(h).containsEntry("delta", 10).containsEntry("date", "2026-09-01"));
    }

    @Test
    void exportOfAJobTheUserDoesNotOwnIs404() {
        when(exportJobRepository.findByIdAndUserId(jobId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getExportData(userId, jobId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
        verifyNoInteractions(cycleLogRepository, pointsHistoryRepository);
    }
}
