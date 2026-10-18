package com.spotit.api.cycle.service;

import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackingStartBackfillTest {
    @Mock UserRepository userRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @InjectMocks TrackingStartBackfill backfill;

    @Test
    void usesTheMonthOfTheLastPeriodOrAnEarlierLoggedPeriod() {
        User onlyLastPeriod = User.builder().id(UUID.randomUUID()).lastPeriodDate(LocalDate.of(2026, 9, 28)).build();
        User olderLog = User.builder().id(UUID.randomUUID()).lastPeriodDate(LocalDate.of(2026, 9, 28)).build();
        when(userRepository.findByTrackingStartDateIsNullAndLastPeriodDateIsNotNull()).thenReturn(List.of(onlyLastPeriod, olderLog));
        when(cycleLogRepository.findFirstByUserIdAndFlowIsNotNullOrderByLogDateAsc(onlyLastPeriod.getId())).thenReturn(Optional.empty());
        when(cycleLogRepository.findFirstByUserIdAndFlowIsNotNullOrderByLogDateAsc(olderLog.getId()))
                .thenReturn(Optional.of(CycleLog.builder().logDate(LocalDate.of(2026, 7, 30)).build()));

        backfill.backfill();

        assertThat(onlyLastPeriod.getTrackingStartDate()).isEqualTo(LocalDate.of(2026, 9, 1));
        assertThat(olderLog.getTrackingStartDate()).isEqualTo(LocalDate.of(2026, 7, 1));
        verify(userRepository).saveAll(List.of(onlyLastPeriod, olderLog));
    }

    @Test
    void doesNothingWhenEveryoneAlreadyHasAStart() {
        when(userRepository.findByTrackingStartDateIsNullAndLastPeriodDateIsNotNull()).thenReturn(List.of());

        backfill.backfill();

        verify(userRepository, never()).saveAll(any());
    }
}
