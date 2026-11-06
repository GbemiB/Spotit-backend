package com.spotit.api.spotlight.games.giveday.service;

import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayLevel;
import com.spotit.api.spotlight.games.giveday.entity.GiveDayRun;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayLevelRepository;
import com.spotit.api.spotlight.games.giveday.repository.GiveDayRunRepository;
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

        GiveDayLevelRepository levels = mock(GiveDayLevelRepository.class);
        when(levels.findByActiveTrueOrderByNumberAsc()).thenReturn(List.of(
                GiveDayLevel.builder().number(1).extrasFree(true).build(),
                GiveDayLevel.builder().number(2).extrasFree(true).build(),
                GiveDayLevel.builder().number(3).passCost(2).build(),
                GiveDayLevel.builder().number(4).extrasFree(true).build()));
        GiveDaySpotlightGame game = new GiveDaySpotlightGame(runs, levels);
        GameSummary summary = game.summary(userId, today);

        assertThat(game.challengeId()).isEqualTo("give-day");
        assertThat(summary).extracting("stars", "levelsWon", "plays").containsExactly(3, 1, 2L);
        assertThat(summary.playDays()).containsExactly(today);
        assertThat(game.freeUpTo()).isEqualTo(2);
    }
}
