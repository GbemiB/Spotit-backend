package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.BadgeResponse;
import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.UserBadge;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeReadServiceImpl implements BadgeReadService {

    private final BadgeDefinitionRepository badgeDefinitionRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeWriteService badgeWriteService;

    @Override
    @Transactional
    public List<BadgeResponse> getBadgesSyncingNewlyEarned(UUID userId) {
        badgeWriteService.syncEarnedBadges(userId);

        List<UserBadge> earned = userBadgeRepository.findByUserId(userId);
        Set<String> earnedIds = earned.stream().map(UserBadge::getBadgeId).collect(Collectors.toSet());
        var earnedAtById = earned.stream().collect(Collectors.toMap(UserBadge::getBadgeId, UserBadge::getEarnedAt));

        return badgeDefinitionRepository.findAll().stream()
                .map(def -> new BadgeResponse(def.getId(), def.getName(),
                        earnedIds.contains(def.getId()), earnedAtById.get(def.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeDefinitionAdminResponse> listDefinitionsForAdmin() {
        return badgeDefinitionRepository.findAll().stream().map(this::toAdminResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BadgeDefinitionAdminResponse getDefinitionForAdmin(String id) {
        return badgeDefinitionRepository.findById(id)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Badge definition not found."));
    }

    private BadgeDefinitionAdminResponse toAdminResponse(BadgeDefinition def) {
        return new BadgeDefinitionAdminResponse(def.getId(), def.getName(), def.getDescription());
    }
}
