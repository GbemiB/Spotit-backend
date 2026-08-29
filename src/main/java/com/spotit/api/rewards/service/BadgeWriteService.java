package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;

import java.util.UUID;

public interface BadgeWriteService {
    void syncEarnedBadges(UUID userId);

    BadgeDefinitionAdminResponse createDefinition(CreateBadgeDefinitionRequest request);

    BadgeDefinitionAdminResponse updateDefinition(String id, UpdateBadgeDefinitionRequest request);

    void deleteDefinition(String id);
}
