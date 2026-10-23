package com.spotit.api.partner.dto;

import com.spotit.api.log.dto.EnumOption;

import java.util.List;

public record PartnerCheckInResponse(
        List<EnumOption> options,
        boolean checkedInToday,
        List<String> supports,
        int reward,
        int streak,
        int pointsAwarded,
        long newBalance
) {
}
