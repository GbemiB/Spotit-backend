package com.spotit.api.insight.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.insight.dto.CycleTrendsResponse;
import com.spotit.api.insight.dto.RegularityResponse;
import com.spotit.api.insight.dto.WeeklyDigestResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import com.spotit.api.log.entity.MoodType;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsightReadServiceImplTest {
    @Mock CycleLogRepository cycleLogRepository;
    @Mock UserRepository userRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks InsightReadServiceImpl service;

    UUID userId = UUID.randomUUID();
    LocalDate today = LocalDate.now();

    private void stubUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).cycleLength(28).periodLength(5).build()));
    }

    /** Flow logs for consecutive-day periods starting {@code daysAgo} days back, each {@code length} days long. */
    private void stubPeriods(int length, int... daysAgo) {
        List<CycleLog> logs = new ArrayList<>();
        for (int start : daysAgo) {
            for (int d = 0; d < length; d++) {
                logs.add(CycleLog.builder().userId(userId).logDate(today.minusDays(start - d)).flow(FlowIntensity.medium).build());
            }
        }
        // A non-flow log in between must not split or extend an episode.
        logs.add(CycleLog.builder().userId(userId).logDate(today.minusDays(1)).mood(MoodType.calm).build());
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(eq(userId), any(), any())).thenReturn(logs);
    }

    @Test
    void trendsComputeCycleAndPeriodAveragesFromLoggedPeriods() {
        stubUser();
        stubPeriods(4, 90, 62, 30);

        CycleTrendsResponse trends = service.getTrends(userId, 6);

        assertThat(trends.cycleLengths()).containsExactly(28, 32);
        assertThat(trends.avgCycleLength()).isEqualTo(30);
        assertThat(trends.avgPeriodLength()).isEqualTo(4);
        assertThat(trends.variationDays()).isEqualTo(2);
    }

    @Test
    void trendsLimitToTheMostRecentCycles() {
        stubUser();
        stubPeriods(4, 90, 62, 30);

        assertThat(service.getTrends(userId, 1).cycleLengths()).containsExactly(32);
    }

    @Test
    void trendsUseTheConfiguredDefaultCycleCount() {
        stubUser();
        stubPeriods(4, 90, 62, 30);
        when(configurationDomainService.getInsightDefaultCycles()).thenReturn(6);

        assertThat(service.getTrends(userId, null).cycleLengths()).hasSize(2);
    }

    @Test
    void trendsWithoutHistoryFallBackToTheProfile() {
        stubUser();
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(eq(userId), any(), any())).thenReturn(List.of());

        assertThat(service.getTrends(userId, 6)).isEqualTo(new CycleTrendsResponse(List.of(28), 28, 5, 0));
    }

    @Test
    void trendsForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTrends(userId, 6)).isInstanceOf(ApiException.class);
    }

    @Test
    void weeklyDigestCountsLogsAndPicksTheMostCommonMood() {
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, today.minusDays(6), today)).thenReturn(List.of(
                CycleLog.builder().logDate(today).mood(MoodType.calm).build(),
                CycleLog.builder().logDate(today.minusDays(1)).mood(MoodType.calm).build(),
                CycleLog.builder().logDate(today.minusDays(2)).mood(MoodType.sad).build(),
                CycleLog.builder().logDate(today.minusDays(3)).build()));

        assertThat(service.getWeeklyDigest(userId)).isEqualTo(new WeeklyDigestResponse(4, "calm", today.minusDays(6), today));
    }

    @Test
    void weeklyDigestWithNoMoodsHasNoTopMood() {
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, today.minusDays(6), today)).thenReturn(List.of());

        WeeklyDigestResponse digest = service.getWeeklyDigest(userId);

        assertThat(digest.loggedCount()).isZero();
        assertThat(digest.topMood()).isNull();
    }

    @Test
    void regularityWithoutAnyPeriodIsInsufficientData() {
        stubUser();
        when(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(eq(userId), any(), any())).thenReturn(List.of());

        RegularityResponse response = service.getRegularity(userId);

        assertThat(response.status()).isEqualTo("insufficient_data");
        assertThat(response.flags()).isEmpty();
    }

    @Test
    void steadyCyclesAreRegular() {
        stubUser();
        stubPeriods(5, 84, 56, 28);
        when(configurationDomainService.getInsightDefaultCycles()).thenReturn(6);
        when(configurationDomainService.getInsightIrregularVariationThresholdDays()).thenReturn(7);
        when(configurationDomainService.getInsightUnusualPeriodLengthDeltaDays()).thenReturn(2);

        RegularityResponse response = service.getRegularity(userId);

        assertThat(response.status()).isEqualTo("regular");
        assertThat(response.disclaimer()).isEqualTo("This is not medical advice.");
    }

    @Test
    void widelyVaryingCyclesAreIrregular() {
        stubUser();
        stubPeriods(5, 100, 78, 40);
        when(configurationDomainService.getInsightDefaultCycles()).thenReturn(6);
        when(configurationDomainService.getInsightIrregularVariationThresholdDays()).thenReturn(7);
        when(configurationDomainService.getInsightUnusualPeriodLengthDeltaDays()).thenReturn(2);

        RegularityResponse response = service.getRegularity(userId);

        assertThat(response.status()).isEqualTo("irregular_cycle");
        assertThat(response.flags()).singleElement().asString().contains("more than 7 days");
    }

    @Test
    void anUnusuallyLongPeriodIsFlagged() {
        stubUser();
        stubPeriods(9, 30);
        when(configurationDomainService.getInsightDefaultCycles()).thenReturn(6);
        when(configurationDomainService.getInsightUnusualPeriodLengthDeltaDays()).thenReturn(2);

        assertThat(service.getRegularity(userId).status()).isEqualTo("unusual_period_length");
    }

    @Test
    void bothProblemsTogetherAreReportedAsIrregular() {
        stubUser();
        stubPeriods(9, 100, 78, 40);
        when(configurationDomainService.getInsightDefaultCycles()).thenReturn(6);
        when(configurationDomainService.getInsightIrregularVariationThresholdDays()).thenReturn(7);
        when(configurationDomainService.getInsightUnusualPeriodLengthDeltaDays()).thenReturn(2);

        RegularityResponse response = service.getRegularity(userId);

        assertThat(response.status()).isEqualTo("irregular_cycle");
        assertThat(response.flags()).hasSize(2);
    }
}
