package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;
import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.UserBadge;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BadgeWriteServiceImpl implements BadgeWriteService {

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeDefinitionRepository badgeDefinitionRepository;
    private final CycleLogRepository cycleLogRepository;
    private final UserRepository userRepository;
    private final ConfigurationDomainService configurationDomainService;

    @Override
    @Transactional
    public void syncEarnedBadges(UUID userId) {
        long logCount = cycleLogRepository.countByUserId(userId);
        int longestStreak = userRepository.findById(userId).map(u -> u.getLongestStreak()).orElse(0);

        awardIfEarned(userId, "first_flow", logCount >= 1);
        awardIfEarned(userId, "know_your_body", logCount >= configurationDomainService.getBadgeKnowYourBodyThreshold());
        awardIfEarned(userId, "cycle_veteran", logCount >= configurationDomainService.getBadgeCycleVeteranThreshold());
        awardIfEarned(userId, "week_warrior", longestStreak >= configurationDomainService.getBadgeWeekWarriorStreakThreshold());
        // ovulation_oracle, health_nerd: intentionally never awarded — no underlying feature yet.
    }

    private void awardIfEarned(UUID userId, String badgeId, boolean earned) {
        if (earned && !userBadgeRepository.existsByUserIdAndBadgeId(userId, badgeId)) {
            userBadgeRepository.save(UserBadge.builder()
                    .userId(userId)
                    .badgeId(badgeId)
                    .earnedAt(Instant.now())
                    .build());
        }
    }

    @Override
    @Transactional
    public BadgeDefinitionAdminResponse createDefinition(CreateBadgeDefinitionRequest request) {
        if (badgeDefinitionRepository.existsById(request.id())) {
            throw new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, ErrorMessage.badgeAlreadyExists(request.id()));
        }
        BadgeDefinition def = new BadgeDefinition(request.id(), request.name(), request.description());
        badgeDefinitionRepository.save(def);
        return toAdminResponse(def);
    }

    @Override
    @Transactional
    public BadgeDefinitionAdminResponse updateDefinition(String id, UpdateBadgeDefinitionRequest request) {
        BadgeDefinition def = badgeDefinitionRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.BADGE_NOT_FOUND));
        if (request.name() != null) def.setName(request.name());
        if (request.description() != null) def.setDescription(request.description());
        badgeDefinitionRepository.save(def);
        return toAdminResponse(def);
    }

    @Override
    @Transactional
    public void deleteDefinition(String id) {
        if (!badgeDefinitionRepository.existsById(id)) {
            throw new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.BADGE_NOT_FOUND);
        }
        badgeDefinitionRepository.deleteById(id);
    }

    private BadgeDefinitionAdminResponse toAdminResponse(BadgeDefinition def) {
        return new BadgeDefinitionAdminResponse(def.getId(), def.getName(), def.getDescription());
    }
}
