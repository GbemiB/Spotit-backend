package com.spotit.api.spotlight.padbank.dto;

import java.util.List;
import java.util.Map;

public record PadBankServeResponse(
        int round,
        String result,
        String tip,
        String speech,
        int hearts,
        int budget,
        int spent,
        int served,
        int right,
        Map<String, Integer> stock,
        boolean finished,
        int stars,
        List<Boolean> goals,
        boolean badge
) {
}
