package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.BadgeResponse;

import java.util.List;
import java.util.UUID;

public interface BadgeReadService {

    /** Re-evaluates badge-earning (a write, delegated to BadgeWriteService) before rendering the current list. */
    List<BadgeResponse> getBadgesSyncingNewlyEarned(UUID userId);

    List<BadgeDefinitionAdminResponse> listDefinitionsForAdmin();

    BadgeDefinitionAdminResponse getDefinitionForAdmin(String id);
}
