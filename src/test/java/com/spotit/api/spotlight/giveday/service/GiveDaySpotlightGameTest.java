package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.giveday.repository.GiveDayRunRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GiveDaySpotlightGameTest {
    @Test
    void theGameReportsBestStarsPerPlacePlaysAndRecentDays() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.of(2026, 11, 2);
        GiveDayRunRepository runs = mock(GiveDayRunRepository.class);
        when(runs.findByUserId(userId)).thenReturn(List.of(
                GiveDayRun.builder().visits("hope-house:2,girls-fc:0").playedOn(today).build(),
                GiveDayRun.builder().visits("hope-house:3").playedOn(today.minusDays(500)).build()));

        GiveDaySpotlightGame game = new GiveDaySpotlightGame(runs);
        GameSummary summary = game.summary(userId, today);

        assertThat(game.challengeId()).isEqualTo("give-day");
        assertThat(summary).extracting("stars", "levelsWon", "plays").containsExactly(3, 1, 2L);
        assertThat(summary.playDays()).containsExactly(today);
    }
}
