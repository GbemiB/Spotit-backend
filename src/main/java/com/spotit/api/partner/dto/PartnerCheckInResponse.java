package com.spotit.api.partner.dto;

import com.spotit.api.log.dto.EnumOption;

import java.util.List;
import java.util.Map;

public record PartnerCheckInResponse(
        List<EnumOption> options,
        boolean checkedInToday,
        List<String> supports,
        int reward,
        int streak,
        int pointsAwarded,
        long newBalance,
        String note,
        List<EnumOption> expressions,
        String kind,
        String expression,
        int sentToday,
        Map<String, Integer> sentByKind
) {
}
