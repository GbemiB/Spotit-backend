package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.ChallengeClaimResponse;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.UserChallengeProgress;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChallengeWriteServiceImplTest {
    @Mock ChallengeDefinitionRepository challengeDefinitionRepository;
    @Mock UserChallengeProgressRepository progressRepository;
    @Mock ChallengeCalculator calculator;
    @Mock PointsWriteService pointsWriteService;

    ChallengeWriteServiceImpl service;
    UUID userId;
    LocalDate weekStart;
    ChallengeDefinition definition;

    @BeforeEach
    void setUp() {
        service = new ChallengeWriteServiceImpl(challengeDefinitionRepository, progressRepository, calculator, pointsWriteService);
        userId = UUID.randomUUID();
        weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        definition = ChallengeDefinition.builder().id("log_week").title("Log every day").reward(150).total(7).build();
    }

    @Test
    void claimingAnUnknownChallengeIsRejected() {
        when(challengeDefinitionRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.claim(userId, "missing"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void claimingBeforeTheChallengeIsCompleteIsRejected() {
        when(challengeDefinitionRepository.findById("log_week")).thenReturn(Optional.of(definition));
        when(calculator.currentWeekStart()).thenReturn(weekStart);
        when(calculator.computeDone(userId, definition)).thenReturn(3);

        assertThatThrownBy(() -> service.claim(userId, "log_week"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_YET_COMPLETE);
        verifyNoInteractions(pointsWriteService);
    }

    @Test
    void claimingAnAlreadyClaimedChallengeIsRejected() {
        when(challengeDefinitionRepository.findById("log_week")).thenReturn(Optional.of(definition));
        when(calculator.currentWeekStart()).thenReturn(weekStart);
        when(calculator.computeDone(userId, definition)).thenReturn(7);
        UserChallengeProgress alreadyClaimed = UserChallengeProgress.builder()
                .userId(userId).challengeId("log_week").weekStartDate(weekStart).claimed(true).build();
        when(progressRepository.findByUserIdAndChallengeIdAndWeekStartDate(userId, "log_week", weekStart))
                .thenReturn(Optional.of(alreadyClaimed));

        assertThatThrownBy(() -> service.claim(userId, "log_week"))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.ALREADY_CLAIMED);
        verifyNoInteractions(pointsWriteService);
    }

    @Test
    void completingAndClaimingForTheFirstTimeAwardsTheReward() {
        when(challengeDefinitionRepository.findById("log_week")).thenReturn(Optional.of(definition));
        when(calculator.currentWeekStart()).thenReturn(weekStart);
        when(calculator.computeDone(userId, definition)).thenReturn(7);
        when(progressRepository.findByUserIdAndChallengeIdAndWeekStartDate(userId, "log_week", weekStart))
                .thenReturn(Optional.empty());
        when(pointsWriteService.adjust(eq(userId), eq(150), any(), any())).thenReturn(650L);

        ChallengeClaimResponse response = service.claim(userId, "log_week");

        assertThat(response.pointsAwarded()).isEqualTo(150);
        assertThat(response.newBalance()).isEqualTo(650);
        verify(progressRepository).save(argThat(UserChallengeProgress::isClaimed));
    }
}
