package com.spotit.api.log.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.log.dto.LogPeriodRequest;
import com.spotit.api.log.dto.LogPeriodResponse;
import com.spotit.api.log.dto.SaveLogRequest;
import com.spotit.api.log.dto.SaveLogResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogWriteServiceImplTest {
    @Mock CycleLogRepository cycleLogRepository;
    @Mock PointsWriteService pointsWriteService;
    @Mock UserRepository userRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @Mock UserClock userClock;

    LogWriteServiceImpl service;
    UUID userId;
    LocalDate date;

    @BeforeEach
    void setUp() {
        service = new LogWriteServiceImpl(cycleLogRepository, pointsWriteService, userRepository, configurationDomainService, userClock);
        lenient().when(userClock.today(any())).thenReturn(LocalDate.of(2026, 12, 31));
        userId = UUID.randomUUID();
        date = LocalDate.of(2026, 7, 28);

        lenient().when(cycleLogRepository.findByUserIdAndFlowIsNotNull(any())).thenReturn(List.of());

        lenient().when(configurationDomainService.getLogMaxPeriodRangeDays()).thenReturn(14);
    }

    private User userWith(LocalDate lastPeriodDate, int cycleLength) {
        return User.builder().id(userId).lastPeriodDate(lastPeriodDate).cycleLength(cycleLength).periodLength(5)
                .points(100).streak(3).build();
    }

    @Test
    void savingANewEntryTellsPointsServiceItIsNew() {
        when(cycleLogRepository.findByUserIdAndLogDate(userId, date)).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordDailyLog(eq(userId), eq(date), eq(true)))
                .thenReturn(new PointsWriteService.LogPointsResult(10, 110, 3, true));

        SaveLogRequest request = new SaveLogRequest("medium", "happy", List.of("cramps", "headache"), "feeling okay", false);
        SaveLogResponse response = service.saveLog(userId, date, request);

        assertThat(response.isNewEntry()).isTrue();
        assertThat(response.flow()).isEqualTo("medium");
        assertThat(response.mood()).isEqualTo("happy");
        assertThat(response.symptoms()).containsExactlyInAnyOrder("cramps", "headache");
        assertThat(response.notes()).isEqualTo("feeling okay");
        assertThat(response.pointsAwarded()).isEqualTo(10);
        assertThat(response.newBalance()).isEqualTo(110);
        assertThat(response.streak()).isEqualTo(3);
    }

    @Test
    void editingAnExistingEntryTellsPointsServiceItIsNotNew() {
        CycleLog existing = CycleLog.builder().id(UUID.randomUUID()).userId(userId).logDate(date)
                .flow(com.spotit.api.log.entity.FlowIntensity.light).build();
        when(cycleLogRepository.findByUserIdAndLogDate(userId, date)).thenReturn(Optional.of(existing));
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordDailyLog(eq(userId), eq(date), eq(false)))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, false));

        SaveLogRequest request = new SaveLogRequest("heavy", null, List.of(), null, true);
        SaveLogResponse response = service.saveLog(userId, date, request);

        assertThat(response.isNewEntry()).isFalse();
        assertThat(response.pointsAwarded()).isZero();
        verify(pointsWriteService).recordDailyLog(userId, date, false);
    }

    @Test
    void nullFlowAndMoodAreStoredAsNull() {
        when(cycleLogRepository.findByUserIdAndLogDate(userId, date)).thenReturn(Optional.empty());
        ArgumentCaptor<CycleLog> captor = ArgumentCaptor.forClass(CycleLog.class);
        when(cycleLogRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordDailyLog(any(), any(), anyBoolean()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 0, 0, true));

        service.saveLog(userId, date, new SaveLogRequest(null, null, null, null, false));

        assertThat(captor.getValue().getFlow()).isNull();
        assertThat(captor.getValue().getMood()).isNull();
        assertThat(captor.getValue().getSymptoms()).isEmpty();
    }

    @Test
    void deletingALogDelegatesToTheRepository() {
        service.deleteLog(userId, date);

        verify(cycleLogRepository).deleteByUserIdAndLogDate(userId, date);
    }

    @Test
    void logPeriodRejectsEndDateBeforeStartDate() {
        LogPeriodRequest request = new LogPeriodRequest(date, date.minusDays(1), null, "medium", null, List.of(), null, false);

        assertThatThrownBy(() -> service.logPeriod(userId, request))
                .isInstanceOf(com.spotit.api.common.exception.ApiException.class);
    }

    @Test
    void logPeriodRejectsRangesLongerThanFourteenDays() {
        LogPeriodRequest request = new LogPeriodRequest(date, date.plusDays(14), null, "medium", null, List.of(), null, false);

        assertThatThrownBy(() -> service.logPeriod(userId, request))
                .isInstanceOf(com.spotit.api.common.exception.ApiException.class);
    }

    @Test
    void logPeriodSetsFlowOnlyOnContinuationDaysAndFullDetailOnStartDate() {
        LocalDate endDate = date.plusDays(2);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(null, 28)));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        ArgumentCaptor<CycleLog> captor = ArgumentCaptor.forClass(CycleLog.class);
        when(cycleLogRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));

        LogPeriodRequest request = new LogPeriodRequest(date, endDate, null, "medium", "calm", List.of("cramps"), "notes", true);
        LogPeriodResponse response = service.logPeriod(userId, request);

        List<CycleLog> saved = captor.getAllValues();
        assertThat(saved).hasSize(3);
        CycleLog startDay = saved.get(0);
        assertThat(startDay.getLogDate()).isEqualTo(date);
        assertThat(startDay.getMood()).isNotNull();
        assertThat(startDay.getNotes()).isEqualTo("notes");
        assertThat(startDay.isIntimate()).isTrue();

        for (CycleLog continuationDay : saved.subList(1, saved.size())) {
            assertThat(continuationDay.getFlow()).isEqualTo(com.spotit.api.log.entity.FlowIntensity.medium);
            assertThat(continuationDay.getMood()).isNull();
            assertThat(continuationDay.getNotes()).isNull();
            assertThat(continuationDay.isIntimate()).isFalse();
        }

        assertThat(response.lastPeriodDate()).isEqualTo(date);
    }

    @Test
    void logPeriodAttachesFullDetailToDetailDateEvenWhenItIsTheEndDate() {
        LocalDate endDate = date.plusDays(2);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(null, 28)));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        ArgumentCaptor<CycleLog> captor = ArgumentCaptor.forClass(CycleLog.class);
        when(cycleLogRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));

        LogPeriodRequest request = new LogPeriodRequest(date, endDate, endDate, "medium", "calm", List.of("cramps"), "period ending", true);
        LogPeriodResponse response = service.logPeriod(userId, request);

        List<CycleLog> saved = captor.getAllValues();
        assertThat(saved).hasSize(3);
        CycleLog startDay = saved.get(0);
        assertThat(startDay.getMood()).isNull();
        assertThat(startDay.getNotes()).isNull();
        assertThat(startDay.isIntimate()).isFalse();

        CycleLog lastDay = saved.get(2);
        assertThat(lastDay.getLogDate()).isEqualTo(endDate);
        assertThat(lastDay.getMood()).isNotNull();
        assertThat(lastDay.getNotes()).isEqualTo("period ending");
        assertThat(lastDay.isIntimate()).isTrue();

        assertThat(response.startDayEntry().date()).isEqualTo(endDate);
    }

    @Test
    void logPeriodAwardsTheDailyRewardOnceRegardlessOfRangeLength() {
        LocalDate endDate = date.plusDays(4);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(null, 28)));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(userId))
                .thenReturn(new PointsWriteService.LogPointsResult(10, 110, 1, true));

        LogPeriodRequest request = new LogPeriodRequest(date, endDate, null, "medium", null, List.of(), null, false);
        LogPeriodResponse response = service.logPeriod(userId, request);

        assertThat(response.pointsAwarded()).isEqualTo(10);
        assertThat(response.newBalance()).isEqualTo(110);
        verify(pointsWriteService, times(1)).recordPeriodLog(any());
    }

    @Test
    void loggingAPeriodClearsOnlyTheRestOfTheSamePeriodAndKeepsEarlierPeriods() {
        LocalDate aug1 = LocalDate.of(2026, 8, 1);
        LocalDate aug2 = LocalDate.of(2026, 8, 2);
        LocalDate may5 = LocalDate.of(2026, 5, 5);
        LocalDate newStart = LocalDate.of(2026, 8, 3);
        LocalDate newEnd = LocalDate.of(2026, 8, 7);

        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(newStart, 28)));
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(10, 110, 1, true));

        CycleLog staleAug1 = CycleLog.builder().userId(userId).logDate(aug1)
                .flow(com.spotit.api.log.entity.FlowIntensity.medium).build();
        CycleLog staleAug2 = CycleLog.builder().userId(userId).logDate(aug2)
                .flow(com.spotit.api.log.entity.FlowIntensity.medium).build();
        CycleLog staleMay = CycleLog.builder().userId(userId).logDate(may5)
                .flow(com.spotit.api.log.entity.FlowIntensity.medium).build();
        when(cycleLogRepository.findByUserIdAndFlowIsNotNull(userId)).thenReturn(List.of(staleAug1, staleAug2, staleMay));

        LogPeriodRequest request = new LogPeriodRequest(newStart, newEnd, null, "medium", null, List.of(), null, false);
        LogPeriodResponse response = service.logPeriod(userId, request);

        verify(cycleLogRepository).delete(staleAug1);
        verify(cycleLogRepository).delete(staleAug2);
        verify(cycleLogRepository, never()).delete(staleMay);

        assertThat(response.clearedEntries()).hasSize(2);
        assertThat(response.clearedEntries()).extracting(com.spotit.api.log.dto.LogEntryResponse::date)
                .containsExactlyInAnyOrder(aug1, aug2);
        assertThat(response.clearedEntries()).allSatisfy(e -> assertThat(e.flow()).isNull());
    }

    @Test
    void loggingANewPeriodPreservesAStaleDayThatHasOtherData() {
        LocalDate aug1 = LocalDate.of(2026, 8, 1);
        LocalDate newStart = LocalDate.of(2026, 8, 2);
        LocalDate newEnd = LocalDate.of(2026, 8, 6);

        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(newStart, 28)));
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(10, 110, 1, true));

        CycleLog staleAug1 = CycleLog.builder().userId(userId).logDate(aug1)
                .flow(com.spotit.api.log.entity.FlowIntensity.medium).notes("felt off today").build();
        when(cycleLogRepository.findByUserIdAndFlowIsNotNull(userId)).thenReturn(List.of(staleAug1));

        LogPeriodRequest request = new LogPeriodRequest(newStart, newEnd, null, "medium", null, List.of(), null, false);
        LogPeriodResponse response = service.logPeriod(userId, request);

        verify(cycleLogRepository, never()).delete(staleAug1);
        assertThat(staleAug1.getFlow()).isNull();
        assertThat(staleAug1.getNotes()).isEqualTo("felt off today");

        assertThat(response.clearedEntries()).hasSize(1);
        assertThat(response.clearedEntries().get(0).date()).isEqualTo(aug1);
        assertThat(response.clearedEntries().get(0).flow()).isNull();
        assertThat(response.clearedEntries().get(0).notes()).isEqualTo("felt off today");
    }

    @Test
    void logPeriodRejectsDetailDateOutsideTheRange() {
        LogPeriodRequest request = new LogPeriodRequest(date, date.plusDays(2), date.plusDays(5), "medium", null, List.of(), null, false);

        assertThatThrownBy(() -> service.logPeriod(userId, request))
                .isInstanceOf(com.spotit.api.common.exception.ApiException.class);
    }

    @Test
    void logPeriodResyncsLastPeriodDateForNearbyCorrection() {
        User user = userWith(date, 28);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));

        LocalDate correctedStart = date.minusDays(2);
        LogPeriodRequest request = new LogPeriodRequest(correctedStart, correctedStart, null, "medium", null, List.of(), null, false);

        LogPeriodResponse response = service.logPeriod(userId, request);

        assertThat(response.lastPeriodDate()).isEqualTo(correctedStart);
    }

    @Test
    void loggingAnOlderSeparatePeriodKeepsTheMostRecentAsTheLastPeriod() {
        LocalDate current = LocalDate.of(2026, 7, 28);
        User user = userWith(current, 28);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));

        LocalDate oldBackfillStart = current.minusMonths(2);
        LogPeriodRequest request = new LogPeriodRequest(oldBackfillStart, oldBackfillStart, null, "medium", null, List.of(), null, false);

        LogPeriodResponse response = service.logPeriod(userId, request);

        assertThat(response.lastPeriodDate()).isEqualTo(current);
        assertThat(response.trackingStartDate()).isEqualTo(oldBackfillStart.withDayOfMonth(1));
        verify(userRepository).save(user);
    }

    @Test
    void shorteningThePeriodClearsTheDaysAfterTheNewEnd() {
        LocalDate start = LocalDate.of(2026, 8, 3);
        User user = userWith(start, 28);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));
        List<CycleLog> old = new java.util.ArrayList<>();
        for (int d = 3; d <= 7; d++) {
            old.add(CycleLog.builder().userId(userId).logDate(LocalDate.of(2026, 8, d)).flow(com.spotit.api.log.entity.FlowIntensity.medium).build());
        }
        when(cycleLogRepository.findByUserIdAndFlowIsNotNull(userId)).thenReturn(old);

        LogPeriodResponse response = service.logPeriod(userId,
                new LogPeriodRequest(start, LocalDate.of(2026, 8, 5), null, "medium", null, List.of(), null, false));

        assertThat(response.clearedEntries()).extracting(com.spotit.api.log.dto.LogEntryResponse::date)
                .containsExactlyInAnyOrder(LocalDate.of(2026, 8, 6), LocalDate.of(2026, 8, 7));
        assertThat(response.lastPeriodDate()).isEqualTo(start);
    }

    @Test
    void loggingTheNextPeriodKeepsThePreviousOneAndBecomesTheLastPeriod() {
        LocalDate previous = LocalDate.of(2026, 7, 6);
        User user = userWith(previous, 28);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));
        CycleLog july = CycleLog.builder().userId(userId).logDate(previous).flow(com.spotit.api.log.entity.FlowIntensity.medium).build();
        when(cycleLogRepository.findByUserIdAndFlowIsNotNull(userId)).thenReturn(List.of(july));

        LocalDate next = LocalDate.of(2026, 8, 3);
        LogPeriodResponse response = service.logPeriod(userId, new LogPeriodRequest(next, next.plusDays(4), null, "medium", null, List.of(), null, false));

        verify(cycleLogRepository, never()).delete(july);
        assertThat(response.clearedEntries()).isEmpty();
        assertThat(response.lastPeriodDate()).isEqualTo(next);
    }

    @Test
    void aPeriodCannotStartAfterTheUsersToday() {
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(LocalDate.of(2026, 10, 5), 28)));
        when(userClock.today(any())).thenReturn(LocalDate.of(2026, 10, 18));
        LocalDate tomorrow = LocalDate.of(2026, 10, 19);

        assertThatThrownBy(() -> service.logPeriod(userId, new LogPeriodRequest(tomorrow, tomorrow.plusDays(4), null, "medium", null, List.of(), null, false)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        verify(cycleLogRepository, never()).save(any(CycleLog.class));
    }

    @Test
    void aPeriodStartingTodayMayEndInTheComingDays() {
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(userWith(LocalDate.of(2026, 9, 20), 28)));
        when(userClock.today(any())).thenReturn(LocalDate.of(2026, 10, 18));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any())).thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));
        LocalDate today = LocalDate.of(2026, 10, 18);

        LogPeriodResponse response = service.logPeriod(userId, new LogPeriodRequest(today, today.plusDays(4), null, "medium", null, List.of(), null, false));

        assertThat(response.lastPeriodDate()).isEqualTo(today);
    }

    @Test
    void logPeriodKeepsAnEarlierTrackingStart() {
        User user = userWith(LocalDate.of(2026, 7, 28), 28);
        user.setTrackingStartDate(LocalDate.of(2026, 5, 1));
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));

        LocalDate next = LocalDate.of(2026, 8, 25);
        service.logPeriod(userId, new LogPeriodRequest(next, next, null, "medium", null, List.of(), null, false));

        assertThat(user.getTrackingStartDate()).isEqualTo(LocalDate.of(2026, 5, 1));
    }

    @Test
    void logPeriodWithoutAStoredStartUsesTheEarlierOfTheOldAndNewPeriod() {
        User user = userWith(LocalDate.of(2026, 7, 28), 28);
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        when(cycleLogRepository.findByUserIdAndLogDate(any(), any())).thenReturn(Optional.empty());
        when(cycleLogRepository.save(any(CycleLog.class))).thenAnswer(inv -> inv.getArgument(0));
        when(pointsWriteService.recordPeriodLog(any()))
                .thenReturn(new PointsWriteService.LogPointsResult(0, 100, 3, true));

        LocalDate next = LocalDate.of(2026, 8, 25);
        service.logPeriod(userId, new LogPeriodRequest(next, next, null, "medium", null, List.of(), null, false));

        assertThat(user.getTrackingStartDate()).isEqualTo(LocalDate.of(2026, 7, 1));
    }
}
