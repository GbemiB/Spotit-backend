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
        long newBalance,
        String note
) {
    public PartnerCheckInResponse(List<EnumOption> options, boolean checkedInToday, List<String> supports, int reward, int streak,
                                  int pointsAwarded, long newBalance) {
        this(options, checkedInToday, supports, reward, streak, pointsAwarded, newBalance, null);
    }
}
