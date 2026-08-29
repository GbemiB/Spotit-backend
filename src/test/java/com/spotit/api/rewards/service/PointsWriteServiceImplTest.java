package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointsWriteServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock PointsHistoryRepository pointsHistoryRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @Mock ChallengeReadService challengeReadService;

    PointsWriteServiceImpl service;
    UUID userId;
    LocalDate today;

    @BeforeEach
    void setUp() {
        service = new PointsWriteServiceImpl(userRepository, pointsHistoryRepository, configurationDomainService, challengeReadService);
        userId = UUID.randomUUID();
        today = LocalDate.now(ZoneOffset.UTC);
    }

    private User userWith(long points, int streak, int longestStreak, LocalDate lastLogDate) {
        User user = User.builder()
                .id(userId)
                .points(points)
                .streak(streak)
                .longestStreak(longestStreak)
                .lastLogDate(lastLogDate)
                .build();
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        return user;
    }

    private User userWithPeriodLog(long points, LocalDate lastPeriodLogDate) {
        User user = User.builder().id(userId).points(points).lastPeriodLogDate(lastPeriodLogDate).build();
        when(userRepository.findByIdForUpdate(userId)).thenReturn(Optional.of(user));
        return user;
    }

    // --- recordDailyLog ---

    @Test
    void backfillingAnOlderDateStillAwardsPointsWhenNew() {
        // logDate itself isn't restricted to today — period/day logging both let a user pick
        // any date (e.g. marking a period that started a few days ago). isNewEntry alone is
        // the anti-double-counting signal; the streak math below is about today's app usage,
        // independent of which date was actually logged.
        userWith(100, 3, 5, today.minusDays(1));
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        var result = service.recordDailyLog(userId, today.minusDays(3), true);

        assertThat(result.pointsAwarded()).isEqualTo(10);
        assertThat(result.newBalance()).isEqualTo(110);
        assertThat(result.streak()).isEqualTo(4);
        verify(userRepository).save(any());
    }

    @Test
    void editingAnExistingEntryForTodayAwardsNoPoints() {
        userWith(100, 3, 5, today);

        var result = service.recordDailyLog(userId, today, false);

        assertThat(result.pointsAwarded()).isZero();
        assertThat(result.isNewEntry()).isFalse();
        verify(userRepository, never()).save(any());
    }

    @Test
    void firstEverLogStartsAStreakOfOne() {
        userWith(0, 0, 0, null);
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        var result = service.recordDailyLog(userId, today, true);

        assertThat(result.streak()).isEqualTo(1);
        assertThat(result.pointsAwarded()).isEqualTo(10);
        assertThat(result.newBalance()).isEqualTo(10);
    }

    @Test
    void loggingTheDayAfterTheLastLogContinuesTheStreak() {
        userWith(50, 4, 4, today.minusDays(1));
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        var result = service.recordDailyLog(userId, today, true);

        assertThat(result.streak()).isEqualTo(5);
    }

    @Test
    void aGapSinceTheLastLogResetsTheStreakToOne() {
        userWith(50, 6, 12, today.minusDays(3));
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        var result = service.recordDailyLog(userId, today, true);

        assertThat(result.streak()).isEqualTo(1);
    }

    @Test
    void longestStreakNeverDecreases() {
        User user = userWith(50, 6, 12, today.minusDays(3));
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        service.recordDailyLog(userId, today, true);

        assertThat(user.getLongestStreak()).isEqualTo(12); // new streak (1) doesn't beat the old record
    }

    // --- recordPeriodLog ---

    @Test
    void firstPeriodLogOfTheMonthAwardsPoints() {
        userWithPeriodLog(100, null);
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        var result = service.recordPeriodLog(userId);

        assertThat(result.pointsAwarded()).isEqualTo(10);
        assertThat(result.newBalance()).isEqualTo(110);
        verify(userRepository).save(any());
    }

    @Test
    void aSecondPeriodLogTheSameMonthAwardsNoPoints() {
        // Correcting/updating the same period again later this month must not re-earn.
        userWithPeriodLog(100, today);

        var result = service.recordPeriodLog(userId);

        assertThat(result.pointsAwarded()).isZero();
        assertThat(result.newBalance()).isEqualTo(100);
        verify(userRepository, never()).save(any());
    }

    @Test
    void aPeriodLogInANewCalendarMonthAwardsPointsAgain() {
        userWithPeriodLog(100, today.minusMonths(1));
        when(challengeReadService.getDailyLogReward()).thenReturn(10);

        var result = service.recordPeriodLog(userId);

        assertThat(result.pointsAwarded()).isEqualTo(10);
        assertThat(result.newBalance()).isEqualTo(110);
    }

    // --- claimDaily ---

    @Test
    void claimingTwiceInOneDayIsANoOp() {
        userWith(100, 0, 0, null).setLastClaimedDate(today);

        var result = service.claimDaily(userId);

        assertThat(result.alreadyClaimedToday()).isTrue();
        assertThat(result.pointsAwarded()).isZero();
        verify(userRepository, never()).save(any());
    }

    @Test
    void firstClaimOfTheDayAwardsConfiguredPoints() {
        userWith(100, 0, 0, null).setLastClaimedDate(today.minusDays(1));
        when(configurationDomainService.getPointsDailyClaim()).thenReturn(50);

        var result = service.claimDaily(userId);

        assertThat(result.alreadyClaimedToday()).isFalse();
        assertThat(result.pointsAwarded()).isEqualTo(50);
        assertThat(result.newBalance()).isEqualTo(150);
    }

    // --- watchAd ---

    @Test
    void watchingAnAdBeyondTheDailyLimitIsRejected() {
        userWith(100, 0, 0, null);
        when(configurationDomainService.getAdsDailyLimit()).thenReturn(5);
        when(pointsHistoryRepository.countByUserIdAndOccurredOnAndLabel(userId, today, "Watched a rewarded ad"))
                .thenReturn(5L);

        assertThatThrownBy(() -> service.watchAd(userId))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getErrorCode())
                .isEqualTo(ErrorCode.DAILY_AD_LIMIT_REACHED);
        verify(userRepository, never()).save(any());
    }

    @Test
    void watchingAnAdUnderTheLimitAwardsPoints() {
        userWith(100, 0, 0, null);
        when(configurationDomainService.getAdsDailyLimit()).thenReturn(5);
        when(configurationDomainService.getPointsWatchAd()).thenReturn(25);
        when(pointsHistoryRepository.countByUserIdAndOccurredOnAndLabel(userId, today, "Watched a rewarded ad"))
                .thenReturn(2L);

        var result = service.watchAd(userId);

        assertThat(result.pointsAwarded()).isEqualTo(25);
        assertThat(result.newBalance()).isEqualTo(125);
    }

    // --- adjust ---

    @Test
    void adjustAppliesAPositiveOrNegativeDeltaToTheBalance() {
        userWith(500, 0, 0, null);

        long newBalance = service.adjust(userId, -200, "🎁", "Redeemed something");

        assertThat(newBalance).isEqualTo(300);
        verify(pointsHistoryRepository).save(argThat(entry ->
                entry.getDelta() == -200 && entry.getLabel().equals("Redeemed something")));
    }
}
