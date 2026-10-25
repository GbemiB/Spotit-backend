package com.spotit.api.monitoring.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ReportSummaryResponse(
        LocalDate from,
        LocalDate to,
        Downloads downloads,
        long signups,
        Active active,
        Crashes crashes,
        Failures failedActions,
        LastActivityPage lastActivity
) {
    public record Downloads(long total, long owners, long partners, Map<String, Long> byPlatform) {
    }

    public record Active(long daily, long weekly, long monthly) {
    }

    public record Crashes(long total, List<TopCrash> top) {
    }

    public record TopCrash(String message, long count, String latestAppVersion, Instant lastSeen) {
    }

    public record Failures(long total, Map<String, Long> byAction) {
    }

    public record LastActivityPage(int page, int size, long totalUsers, List<UserActivity> users) {
    }

    public record UserActivity(UUID userId, String firstName, String email, String accountType, Instant lastActiveAt,
                               LocalDate lastLogDate, LocalDate lastPeriodLogDate, Instant lastPartnerCheckInAt) {
    }
}
