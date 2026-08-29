package com.spotit.api.insight.service;

import com.spotit.api.insight.dto.CycleTrendsResponse;
import com.spotit.api.insight.dto.RegularityResponse;
import com.spotit.api.insight.dto.WeeklyDigestResponse;

import java.util.UUID;

public interface InsightReadService {
    CycleTrendsResponse getTrends(UUID userId, Integer cyclesParam);

    WeeklyDigestResponse getWeeklyDigest(UUID userId);

    RegularityResponse getRegularity(UUID userId);
}
