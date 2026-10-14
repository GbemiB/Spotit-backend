package com.spotit.api.user.dto;

import java.util.List;

public record NotificationPrefsResponse(boolean period, boolean ovulation, boolean dailyLog, boolean digest,
                                        String morningTime, String eveningTime,
                                        List<Integer> periodDaysBefore, int maxDaysBefore) {
}
