package com.spotit.api.spotlight.padbank.dto;

import java.util.List;
import java.util.Map;

public record PadBankServeResponse(
        int round,
        String result,
        String title,
        String tip,
        String speech,
        int hearts,
        long balance,
        int spent,
        int spentToday,
        int served,
        int right,
        int rightPack,
        int rightWay,
        Map<String, Integer> stock,
        boolean finished,
        int stars,
        List<Boolean> goals,
        boolean badge
) {
}
