package com.spotit.api.partner.dto;

import java.util.List;
import java.util.Map;

public record PartnerCheckInResponse(
        List<LoveOptionView> options,
        boolean checkedInToday,
        List<String> supports,
        int reward,
        int streak,
        int pointsAwarded,
        long newBalance,
        String note,
        List<LoveOptionView> expressions,
        String kind,
        String expression,
        int sentToday,
        Map<String, Integer> sentByKind,
        List<String> starters,
        List<LoveOptionView> categories,
        HerReply herReply
) {
}
