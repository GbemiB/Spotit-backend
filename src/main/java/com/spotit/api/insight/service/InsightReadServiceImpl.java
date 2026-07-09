package com.spotit.api.insight.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.insight.dto.CycleTrendsResponse;
import com.spotit.api.insight.dto.RegularityResponse;
import com.spotit.api.insight.dto.WeeklyDigestResponse;
import com.spotit.api.log.entity.CycleLog;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Replaces the mobile app's hardcoded trend/regularity placeholders with real
 * computation over logged data: a "period episode" is a run of consecutive
 * days with a flow logged; its first day is treated as a period start, and
 * the gap between consecutive starts is a cycle length.
 */
@Service
@RequiredArgsConstructor
public class InsightReadServiceImpl implements InsightReadService {

    private static final int DEFAULT_CYCLES = 6;
    private static final int IRREGULAR_VARIATION_THRESHOLD_DAYS = 4;
    private static final int UNUSUAL_PERIOD_LENGTH_DELTA_DAYS = 3;

    private final CycleLogRepository cycleLogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CycleTrendsResponse getTrends(UUID userId, Integer cyclesParam) {
        int cycles = cyclesParam == null ? DEFAULT_CYCLES : cyclesParam;
        User user = requireUser(userId);

        List<PeriodEpisode> episodes = detectPeriodEpisodes(userId);
        List<Integer> cycleLengths = cycleLengthsFrom(episodes, cycles);
        List<Integer> periodLengths = episodes.stream()
                .skip(Math.max(0, episodes.size() - cycles))
                .map(PeriodEpisode::lengthDays)
                .toList();

        int avgCycleLength = cycleLengths.isEmpty() ? user.getCycleLength() : average(cycleLengths);
        int avgPeriodLength = periodLengths.isEmpty() ? user.getPeriodLength() : average(periodLengths);
        int variationDays = cycleLengths.size() < 2 ? 0
                : (java.util.Collections.max(cycleLengths) - java.util.Collections.min(cycleLengths) + 1) / 2;

        List<Integer> series = cycleLengths.isEmpty() ? List.of(user.getCycleLength()) : cycleLengths;
        return new CycleTrendsResponse(series, avgCycleLength, avgPeriodLength, variationDays);
    }

    @Override
    @Transactional(readOnly = true)
    public WeeklyDigestResponse getWeeklyDigest(UUID userId) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        List<CycleLog> logs = cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, start, today);

        int loggedCount = logs.size();
        String topMood = logs.stream()
                .filter(l -> l.getMood() != null)
                .collect(Collectors.groupingBy(CycleLog::getMood, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey().name())
                .orElse(null);

        return new WeeklyDigestResponse(loggedCount, topMood, start, today);
    }

    @Override
    @Transactional(readOnly = true)
    public RegularityResponse getRegularity(UUID userId) {
        User user = requireUser(userId);
        List<PeriodEpisode> episodes = detectPeriodEpisodes(userId);
        List<Integer> cycleLengths = cycleLengthsFrom(episodes, DEFAULT_CYCLES);

        List<String> flags = new ArrayList<>();
        if (cycleLengths.size() >= 2) {
            int variation = java.util.Collections.max(cycleLengths) - java.util.Collections.min(cycleLengths);
            if (variation > IRREGULAR_VARIATION_THRESHOLD_DAYS) {
                flags.add("Your recent cycle lengths have varied by more than " + IRREGULAR_VARIATION_THRESHOLD_DAYS + " days.");
            }
        }
        episodes.stream().reduce((a, b) -> b).ifPresent(last -> {
            if (Math.abs(last.lengthDays() - user.getPeriodLength()) > UNUSUAL_PERIOD_LENGTH_DELTA_DAYS) {
                flags.add("Your most recent period lasted longer or shorter than usual.");
            }
        });

        String status = flags.isEmpty() ? "regular" : (flags.size() > 1 ? "irregular_cycle" :
                flags.get(0).contains("period") ? "unusual_period_length" : "irregular_cycle");
        return new RegularityResponse(status, flags, "This is not medical advice.");
    }

    // -- period-episode detection --------------------------------------

    private record PeriodEpisode(LocalDate start, LocalDate end) {
        int lengthDays() {
            return (int) ChronoUnit.DAYS.between(start, end) + 1;
        }
    }

    private List<PeriodEpisode> detectPeriodEpisodes(UUID userId) {
        List<LocalDate> flowDates = cycleLogRepository
                .findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, LocalDate.now().minusYears(2), LocalDate.now())
                .stream()
                .filter(l -> l.getFlow() != null)
                .map(CycleLog::getLogDate)
                .sorted()
                .toList();

        List<PeriodEpisode> episodes = new ArrayList<>();
        LocalDate episodeStart = null;
        LocalDate episodeEnd = null;
        for (LocalDate date : flowDates) {
            if (episodeStart == null) {
                episodeStart = date;
                episodeEnd = date;
            } else if (ChronoUnit.DAYS.between(episodeEnd, date) <= 1) {
                episodeEnd = date;
            } else {
                episodes.add(new PeriodEpisode(episodeStart, episodeEnd));
                episodeStart = date;
                episodeEnd = date;
            }
        }
        if (episodeStart != null) {
            episodes.add(new PeriodEpisode(episodeStart, episodeEnd));
        }
        return episodes;
    }

    private List<Integer> cycleLengthsFrom(List<PeriodEpisode> episodes, int maxCycles) {
        if (episodes.size() < 2) return List.of();
        List<Integer> lengths = new ArrayList<>();
        for (int i = 1; i < episodes.size(); i++) {
            lengths.add((int) ChronoUnit.DAYS.between(episodes.get(i - 1).start(), episodes.get(i).start()));
        }
        return lengths.stream().skip(Math.max(0, lengths.size() - maxCycles)).toList();
    }

    private int average(List<Integer> values) {
        return (int) Math.round(values.stream().mapToInt(Integer::intValue).average().orElse(0));
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "User not found."));
    }
}
