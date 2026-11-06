package com.spotit.api.spotlight.games.padbank.dto;

import java.util.List;

public record PadBankCloseResponse(
        int served,
        int rightPack,
        int rightWay,
        int spentToday,
        int stars,
        List<Boolean> goals
) {
}
