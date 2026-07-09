package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.ChallengeClaimResponse;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateChallengeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateChallengeDefinitionRequest;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import com.spotit.api.rewards.entity.UserChallengeProgress;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChallengeWriteServiceImpl implements ChallengeWriteService {

    private final ChallengeDefinitionRepository challengeDefinitionRepository;
    private final UserChallengeProgressRepository progressRepository;
    private final ChallengeCalculator calculator;
    private final PointsWriteService pointsWriteService;

    @Override
    @Transactional
    public ChallengeClaimResponse claim(UUID userId, String challengeId) {
        ChallengeDefinition def = challengeDefinitionRepository.findById(challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Challenge not found."));
        LocalDate weekStart = calculator.currentWeekStart();
        int done = calculator.computeDone(userId, def);

        if (done < def.getTotal()) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, "This challenge isn't complete yet.");
        }

        UserChallengeProgress progress = progressRepository
                .findByUserIdAndChallengeIdAndWeekStartDate(userId, challengeId, weekStart)
                .orElseGet(() -> UserChallengeProgress.builder()
                        .userId(userId).challengeId(challengeId).weekStartDate(weekStart).claimed(false).build());

        if (progress.isClaimed()) {
            throw new ApiException(ErrorCode.ALREADY_CLAIMED, "This challenge's reward has already been claimed this week.");
        }
        progress.setClaimed(true);
        progress.setClaimedAt(Instant.now());
        progressRepository.save(progress);

        long newBalance = pointsWriteService.adjust(userId, def.getReward(), "🏆", "Completed challenge: " + def.getTitle());
        return new ChallengeClaimResponse(def.getReward(), newBalance);
    }

    @Override
    @Transactional
    public ChallengeDefinitionAdminResponse createDefinition(CreateChallengeDefinitionRequest request) {
        if (challengeDefinitionRepository.existsById(request.id())) {
            throw new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, "A challenge with id '" + request.id() + "' already exists.");
        }
        ChallengeDefinition def = ChallengeDefinition.builder()
                .id(request.id())
                .title(request.title())
                .reward(request.reward())
                .total(request.total())
                .type(ChallengeType.valueOf(request.type()))
                .build();
        challengeDefinitionRepository.save(def);
        return toAdminResponse(def);
    }

    @Override
    @Transactional
    public ChallengeDefinitionAdminResponse updateDefinition(String id, UpdateChallengeDefinitionRequest request) {
        ChallengeDefinition def = challengeDefinitionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Challenge definition not found."));
        if (request.title() != null) def.setTitle(request.title());
        if (request.reward() != null) def.setReward(request.reward());
        if (request.total() != null) def.setTotal(request.total());
        challengeDefinitionRepository.save(def);
        return toAdminResponse(def);
    }

    @Override
    @Transactional
    public void deleteDefinition(String id) {
        if (!challengeDefinitionRepository.existsById(id)) {
            throw new ApiException(ErrorCode.NOT_FOUND, "Challenge definition not found.");
        }
        challengeDefinitionRepository.deleteById(id);
    }

    private ChallengeDefinitionAdminResponse toAdminResponse(ChallengeDefinition def) {
        return new ChallengeDefinitionAdminResponse(def.getId(), def.getTitle(), def.getReward(), def.getTotal(), def.getType().name());
    }
}
