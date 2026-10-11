package com.spotit.api.user.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UpdateNotificationPrefsRequest(
        Boolean period, Boolean ovulation, Boolean dailyLog, Boolean digest,
        @Pattern(regexp = TIME) String morningTime,
        @Pattern(regexp = TIME) String eveningTime,
        // Days before the predicted date, 0 = on the day. An empty list turns that reminder off.
        List<Integer> periodDaysBefore,
        List<Integer> ovulationDaysBefore
) {
    static final String TIME = "([01]\\d|2[0-3]):[0-5]\\d";
}
