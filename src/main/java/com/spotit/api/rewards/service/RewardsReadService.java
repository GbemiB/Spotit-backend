package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.PointsHistoryPageResponse;
import com.spotit.api.rewards.dto.RewardsSummaryResponse;

import java.util.UUID;

public interface RewardsReadService {

    RewardsSummaryResponse getSummary(UUID userId);

    PointsHistoryPageResponse getHistory(UUID userId, Integer limit, String cursor);
}
