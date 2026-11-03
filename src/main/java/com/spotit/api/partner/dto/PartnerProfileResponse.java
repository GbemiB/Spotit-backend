package com.spotit.api.partner.dto;

import java.time.LocalDate;

public record PartnerProfileResponse(
        String firstName,
        String nickname,
        int cycleLength,
        int periodLength,
        LocalDate lastPeriodDate,
        LocalDate trackingStartDate
) {
}
