package com.spotit.api.cycle;

import com.spotit.api.log.entity.CycleLog;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PeriodEpisodes {
    public static final int MAX_GAP_DAYS = 2;

    private PeriodEpisodes() {
    }

    public record Episode(LocalDate start, LocalDate end) {
        public int lengthDays() {
            return (int) ChronoUnit.DAYS.between(start, end) + 1;
        }
    }

    public static boolean sameEpisode(LocalDate earlier, LocalDate later) {
        return ChronoUnit.DAYS.between(earlier, later) <= MAX_GAP_DAYS;
    }

    public static List<Episode> detect(List<CycleLog> logs) {
        List<CycleLog> flowLogs = logs.stream()
                .filter(l -> l.getFlow() != null)
                .sorted(Comparator.comparing(CycleLog::getLogDate))
                .toList();

        List<Episode> episodes = new ArrayList<>();
        LocalDate episodeStart = null;
        LocalDate episodeEnd = null;
        for (CycleLog log : flowLogs) {
            LocalDate date = log.getLogDate();
            if (episodeStart != null && sameEpisode(episodeEnd, date)) {
                episodeEnd = date;
                continue;
            }
            if (episodeStart != null) {
                episodes.add(new Episode(episodeStart, episodeEnd));
                episodeStart = null;
            }
            if (!log.getFlow().isSpotting()) {
                episodeStart = date;
                episodeEnd = date;
            }
        }
        if (episodeStart != null) {
            episodes.add(new Episode(episodeStart, episodeEnd));
        }
        return episodes;
    }
}
