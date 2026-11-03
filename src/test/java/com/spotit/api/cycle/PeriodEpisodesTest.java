package com.spotit.api.cycle;

import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.entity.FlowIntensity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodEpisodesTest {
    private static CycleLog day(int month, int dayOfMonth, FlowIntensity flow) {
        return CycleLog.builder().logDate(LocalDate.of(2026, month, dayOfMonth)).flow(flow).build();
    }

    @Test
    void daysUpToTwoApartFormOnePeriodAndLongerGapsStartANewOne() {
        List<PeriodEpisodes.Episode> episodes = PeriodEpisodes.detect(List.of(
                day(7, 6, FlowIntensity.heavy), day(7, 7, FlowIntensity.medium), day(7, 9, FlowIntensity.light),
                day(8, 3, FlowIntensity.medium), day(8, 4, FlowIntensity.medium)));

        assertThat(episodes).containsExactly(
                new PeriodEpisodes.Episode(LocalDate.of(2026, 7, 6), LocalDate.of(2026, 7, 9)),
                new PeriodEpisodes.Episode(LocalDate.of(2026, 8, 3), LocalDate.of(2026, 8, 4)));
        assertThat(episodes.get(0).lengthDays()).isEqualTo(4);
    }

    @Test
    void spottingAloneDoesNotStartAPeriodAndDaysWithoutFlowAreIgnored() {
        List<PeriodEpisodes.Episode> episodes = PeriodEpisodes.detect(List.of(
                day(7, 1, FlowIntensity.spotting), day(7, 20, null), day(8, 3, FlowIntensity.medium)));

        assertThat(episodes).containsExactly(new PeriodEpisodes.Episode(LocalDate.of(2026, 8, 3), LocalDate.of(2026, 8, 3)));
    }
}
