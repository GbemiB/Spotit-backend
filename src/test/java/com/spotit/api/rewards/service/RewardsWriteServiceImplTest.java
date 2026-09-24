package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.AdWatchResponse;
import com.spotit.api.rewards.dto.DailyClaimResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardsWriteServiceImplTest {
    @Mock PointsWriteService pointsWriteService;
    @InjectMocks RewardsWriteServiceImpl service;

    UUID userId = UUID.randomUUID();

    @Test
    void claimDailyPassesThroughThePointsResult() {
        when(pointsWriteService.claimDaily(userId)).thenReturn(new PointsWriteService.DailyClaimResult(5, 625, false));

        assertThat(service.claimDaily(userId)).isEqualTo(new DailyClaimResponse(5, 625, false));
    }

    @Test
    void claimDailyReportsAnAlreadyClaimedDay() {
        when(pointsWriteService.claimDaily(userId)).thenReturn(new PointsWriteService.DailyClaimResult(0, 620, true));

        assertThat(service.claimDaily(userId).alreadyClaimedToday()).isTrue();
    }

    @Test
    void watchAdPassesThroughThePointsResult() {
        when(pointsWriteService.watchAd(userId)).thenReturn(new PointsWriteService.AdWatchResult(3, 628));

        assertThat(service.watchAd(userId)).isEqualTo(new AdWatchResponse(3, 628));
    }
}
