package com.spotit.api.spotlight.core.service;

import java.time.LocalDate;
import java.util.UUID;

public interface SpotlightGame {
    String challengeId();

    GameSummary summary(UUID userId, LocalDate today);

    default int freeUpTo() {
        return 0;
    }
}
