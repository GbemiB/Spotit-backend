package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.padbank.repository.PadBankRunRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PadBankSpotlightGameTest {
    @Test
    void theGameReportsBestStarsPerDayAndPlayDays() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.of(2026, 11, 2);
        PadBankRunRepository runs = mock(PadBankRunRepository.class);
        when(runs.bestStarsByDay(userId)).thenReturn(List.of(new Object[]{1, 3}, new Object[]{2, 0}));
        when(runs.findPlayDaysSince(eq(userId), any())).thenReturn(List.of(today));
        when(runs.countByUserId(userId)).thenReturn(4L);

        ConfigurationDomainService config = mock(ConfigurationDomainService.class);
        when(config.getPadbankFreeDays()).thenReturn(2);
        PadBankSpotlightGame game = new PadBankSpotlightGame(runs, config);
        GameSummary summary = game.summary(userId, today);

        assertThat(game.challengeId()).isEqualTo("pad-bank-run");
        assertThat(summary).extracting("stars", "levelsWon", "rightAnswers", "plays").containsExactly(3, 1, 0L, 4L);
        assertThat(summary.playDays()).containsExactly(today);
        assertThat(game.freeUpTo()).isEqualTo(2);
    }
}
