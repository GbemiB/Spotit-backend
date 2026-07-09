package com.spotit.api.insight.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.insight.dto.CycleTrendsResponse;
import com.spotit.api.insight.dto.RegularityResponse;
import com.spotit.api.insight.dto.WeeklyDigestResponse;
import com.spotit.api.insight.service.InsightReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class InsightController {

    private final InsightReadService insightReadService;

    @GetMapping("/trends")
    public CycleTrendsResponse trends(@CurrentUserId UUID userId, @RequestParam(required = false) Integer cycles) {
        return insightReadService.getTrends(userId, cycles);
    }

    @GetMapping("/digest/weekly")
    public WeeklyDigestResponse weeklyDigest(@CurrentUserId UUID userId) {
        return insightReadService.getWeeklyDigest(userId);
    }

    @GetMapping("/regularity")
    public RegularityResponse regularity(@CurrentUserId UUID userId) {
        return insightReadService.getRegularity(userId);
    }
}
