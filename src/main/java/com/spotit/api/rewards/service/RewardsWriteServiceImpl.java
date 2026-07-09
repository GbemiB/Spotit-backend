package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.AdWatchResponse;
import com.spotit.api.rewards.dto.DailyClaimResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RewardsWriteServiceImpl implements RewardsWriteService {

    private final PointsWriteService pointsWriteService;

    @Override
    public DailyClaimResponse claimDaily(UUID userId) {
        var result = pointsWriteService.claimDaily(userId);
        return new DailyClaimResponse(result.pointsAwarded(), result.newBalance(), result.alreadyClaimedToday());
    }

    @Override
    public AdWatchResponse watchAd(UUID userId) {
        var result = pointsWriteService.watchAd(userId);
        return new AdWatchResponse(result.pointsAwarded(), result.newBalance());
    }
}
