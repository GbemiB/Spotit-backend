package com.spotit.api.user.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UpdateNotificationPrefsRequest(
        Boolean period, Boolean ovulation, Boolean dailyLog, Boolean digest,
        @Pattern(regexp = TIME) String morningTime,
        @Pattern(regexp = TIME) String eveningTime,
        // Days before the predicted period, 0 = on the day. An empty list turns the period reminder off.
        List<Integer> periodDaysBefore
) {
    static final String TIME = "([01]\\d|2[0-3]):[0-5]\\d";
}
