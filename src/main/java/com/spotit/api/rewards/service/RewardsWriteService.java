package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.AdWatchResponse;
import com.spotit.api.rewards.dto.DailyClaimResponse;

import java.util.UUID;

public interface RewardsWriteService {

    DailyClaimResponse claimDaily(UUID userId);

    AdWatchResponse watchAd(UUID userId);
}
