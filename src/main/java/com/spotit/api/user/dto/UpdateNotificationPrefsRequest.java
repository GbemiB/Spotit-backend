package com.spotit.api.user.dto;

import java.util.List;

public record UpdateNotificationPrefsRequest(
        Boolean period, Boolean ovulation, Boolean dailyLog, Boolean digest,
        // Days before the predicted period, 0 = on the day. An empty list turns the period reminder off.
        List<Integer> periodDaysBefore
) {
}
