package com.spotit.api.user.dto;

import java.util.List;

public record UpdateNotificationPrefsRequest(
        Boolean period, Boolean ovulation, Boolean dailyLog, Boolean digest,
        List<Integer> periodDaysBefore,
        Boolean partnerLove
) {
    public UpdateNotificationPrefsRequest(Boolean period, Boolean ovulation, Boolean dailyLog, Boolean digest, List<Integer> periodDaysBefore) {
        this(period, ovulation, dailyLog, digest, periodDaysBefore, null);
    }
}
