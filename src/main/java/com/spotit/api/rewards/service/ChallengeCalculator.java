package com.spotit.api.rewards.service;

import com.spotit.api.common.time.UserClock;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class ChallengeCalculator {
    private final CycleLogRepository cycleLogRepository;
    private final UserRepository userRepository;
    private final UserClock userClock;

    int computeDone(UUID userId, ChallengeDefinition def) {
        if (def.getType() == ChallengeType.WEEKLY_LOG) {
            LocalDate today = today(userId);
            return cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, today.with(DayOfWeek.MONDAY), today).size();
        }
        return 0;
    }

    LocalDate currentWeekStart(UUID userId) {
        return today(userId).with(DayOfWeek.MONDAY);
    }

    private LocalDate today(UUID userId) {
        return userRepository.findById(userId).map(userClock::today).orElseGet(() -> LocalDate.now(ZoneOffset.UTC));
    }
}
