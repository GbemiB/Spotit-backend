package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.ChallengeResponse;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.UserChallengeProgress;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChallengeReadServiceImpl implements ChallengeReadService {
    private static final String DAILY_LOG_DEFINITION_ID = "daily_log";

    private final ChallengeDefinitionRepository challengeDefinitionRepository;
    private final UserChallengeProgressRepository progressRepository;
    private final ChallengeCalculator calculator;

    @Override
    @Transactional(readOnly = true)
    public List<ChallengeResponse> getChallenges(UUID userId) {
        LocalDate weekStart = calculator.currentWeekStart();
        return challengeDefinitionRepository.findAll().stream()
                .filter(def -> !DAILY_LOG_DEFINITION_ID.equals(def.getId()))
                .map(def -> toResponse(userId, def, weekStart))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public int getDailyLogReward() {
        return challengeDefinitionRepository.findById(DAILY_LOG_DEFINITION_ID)
                .map(ChallengeDefinition::getReward)
                .orElse(80);
    }

    private ChallengeResponse toResponse(UUID userId, ChallengeDefinition def, LocalDate weekStart) {
        int done = calculator.computeDone(userId, def);
        boolean claimed = progressRepository.findByUserIdAndChallengeIdAndWeekStartDate(userId, def.getId(), weekStart)
                .map(UserChallengeProgress::isClaimed).orElse(false);
        return new ChallengeResponse(def.getId(), def.getTitle(), def.getReward(), Math.min(done, def.getTotal()),
                def.getTotal(), done >= def.getTotal(), claimed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChallengeDefinitionAdminResponse> listDefinitionsForAdmin() {
        return challengeDefinitionRepository.findAll().stream().map(this::toAdminResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChallengeDefinitionAdminResponse getDefinitionForAdmin(String id) {
        return challengeDefinitionRepository.findById(id)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.CHALLENGE_DEFINITION_NOT_FOUND));
    }

    private ChallengeDefinitionAdminResponse toAdminResponse(ChallengeDefinition def) {
        return new ChallengeDefinitionAdminResponse(def.getId(), def.getTitle(), def.getReward(), def.getTotal(), def.getType().name());
    }
}
