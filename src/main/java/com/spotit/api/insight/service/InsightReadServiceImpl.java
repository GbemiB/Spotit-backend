package com.spotit.api.insight.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.cycle.PeriodEpisodes;
import com.spotit.api.cycle.PeriodEpisodes.Episode;
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

@Service
@RequiredArgsConstructor
public class InsightReadServiceImpl implements InsightReadService {
    private final CycleLogRepository cycleLogRepository;
    private final UserRepository userRepository;
    private final ConfigurationDomainService configurationDomainService;
    private final UserClock userClock;

    @Override
    @Transactional(readOnly = true)
    public CycleTrendsResponse getTrends(UUID userId, Integer cyclesParam) {
        int cycles = cyclesParam == null ? configurationDomainService.getInsightDefaultCycles() : cyclesParam;
        User user = requireUser(userId);
        LocalDate today = userClock.today(user);

        List<Episode> episodes = detectPeriodEpisodes(userId, today);
        List<Integer> cycleLengths = cycleLengthsFrom(episodes, cycles);
        List<Episode> completed = completedEpisodes(episodes, today);
        List<Integer> periodLengths = completed.stream()
                .skip(Math.max(0, completed.size() - cycles))
                .map(Episode::lengthDays)
                .toList();

        int avgCycleLength = cycleLengths.isEmpty() ? user.getCycleLength() : average(cycleLengths);
        int avgPeriodLength = periodLengths.isEmpty() ? user.getPeriodLength() : average(periodLengths);
        int variationDays = cycleLengths.size() < 2 ? 0
                : (java.util.Collections.max(cycleLengths) - java.util.Collections.min(cycleLengths) + 1) / 2;

        return new CycleTrendsResponse(cycleLengths, avgCycleLength, avgPeriodLength, variationDays);
    }

    @Override
    @Transactional(readOnly = true)
    public WeeklyDigestResponse getWeeklyDigest(UUID userId) {
        LocalDate today = userClock.today(requireUser(userId));
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
        LocalDate today = userClock.today(user);
        List<Episode> episodes = detectPeriodEpisodes(userId, today);

        if (episodes.isEmpty()) {
            return new RegularityResponse("insufficient_data", List.of(), "This is not medical advice.");
        }

        List<Integer> cycleLengths = cycleLengthsFrom(episodes, configurationDomainService.getInsightDefaultCycles());

        List<String> flags = new ArrayList<>();
        if (cycleLengths.size() >= 2) {
            int variation = java.util.Collections.max(cycleLengths) - java.util.Collections.min(cycleLengths);
            int irregularVariationThresholdDays = configurationDomainService.getInsightIrregularVariationThresholdDays();
            if (variation > irregularVariationThresholdDays) {
                flags.add("Your recent cycle lengths have varied by more than " + irregularVariationThresholdDays + " days.");
            }
        }
        completedEpisodes(episodes, today).stream().reduce((a, b) -> b).ifPresent(last -> {
            if (Math.abs(last.lengthDays() - user.getPeriodLength()) > configurationDomainService.getInsightUnusualPeriodLengthDeltaDays()) {
                flags.add("Your most recent period lasted longer or shorter than usual.");
            }
        });

        if (flags.isEmpty() && cycleLengths.size() < 2) {
            return new RegularityResponse("insufficient_data", List.of(), "This is not medical advice.");
        }
        String status = flags.isEmpty() ? "regular" : (flags.size() > 1 ? "irregular_cycle" :
                flags.get(0).contains("period") ? "unusual_period_length" : "irregular_cycle");
        return new RegularityResponse(status, flags, "This is not medical advice.");
    }

    private List<Episode> detectPeriodEpisodes(UUID userId, LocalDate today) {
        return PeriodEpisodes.detect(cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, today.minusYears(2), today));
    }

    private List<Episode> completedEpisodes(List<Episode> episodes, LocalDate today) {
        if (episodes.isEmpty()) {
            return episodes;
        }
        Episode last = episodes.get(episodes.size() - 1);
        boolean ongoing = !last.end().isBefore(today.minusDays(PeriodEpisodes.MAX_GAP_DAYS - 1L));
        return ongoing ? episodes.subList(0, episodes.size() - 1) : episodes;
    }

    private List<Integer> cycleLengthsFrom(List<Episode> episodes, int maxCycles) {
        if (episodes.size() < 2) {
            return List.of();
        }
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
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
