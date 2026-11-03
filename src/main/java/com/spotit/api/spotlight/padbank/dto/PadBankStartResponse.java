package com.spotit.api.spotlight.padbank.dto;

import java.util.List;
import java.util.UUID;

public record PadBankStartResponse(
        UUID runId,
        int day,
        long balance,
        int cost,
        int hearts,
        int freeVisitors,
        int visitorFee,
        int deliveryCost,
        List<PadBankPackView> packs,
        List<PadBankVisitorView> visitors,
        PadBankDayTitles dayTitles
) {
}
