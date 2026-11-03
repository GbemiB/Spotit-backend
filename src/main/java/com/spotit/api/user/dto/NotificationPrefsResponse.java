package com.spotit.api.user.dto;

import java.util.List;

public record NotificationPrefsResponse(boolean period, boolean ovulation, boolean dailyLog, boolean digest,
                                        String morningTime, String eveningTime,
                                        List<Integer> periodDaysBefore, int maxDaysBefore, boolean partnerLove) {
    public NotificationPrefsResponse(boolean period, boolean ovulation, boolean dailyLog, boolean digest,
                                     String morningTime, String eveningTime, List<Integer> periodDaysBefore, int maxDaysBefore) {
        this(period, ovulation, dailyLog, digest, morningTime, eveningTime, periodDaysBefore, maxDaysBefore, true);
    }
}
