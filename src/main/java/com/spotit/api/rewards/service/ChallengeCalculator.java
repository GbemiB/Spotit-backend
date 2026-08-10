package com.spotit.api.rewards.service;

import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

/** Shared by ChallengeReadServiceImpl and ChallengeWriteServiceImpl so "done" is computed identically on both sides of the CQRS split. */
@Component
@RequiredArgsConstructor
class ChallengeCalculator {

    /** Matches CHALLENGES.read_3 in the mobile app — a fixed stand-in until article-reading is tracked. */
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
