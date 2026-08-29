package com.spotit.api.rewards.service;

import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class ChallengeCalculator {
    private static final int READ_3_STUB_DONE = 1;

    private final CycleLogRepository cycleLogRepository;

    int computeDone(UUID userId, ChallengeDefinition def) {
        if (def.getType() == ChallengeType.WEEKLY_LOG) {
            LocalDate today = LocalDate.now();
            return cycleLogRepository.findByUserIdAndLogDateBetweenOrderByLogDateAsc(userId, currentWeekStart(), today).size();
        }
        return READ_3_STUB_DONE;
    }

    LocalDate currentWeekStart() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }
}
