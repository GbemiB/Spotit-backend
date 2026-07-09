package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;

import java.util.UUID;

/**
 * Only 4 of the 6 defined badges have real earning logic — Ovulation Oracle
 * and Health Nerd stay permanently unearned until LH-test logging and an
 * article-reading feature exist (see the requirement doc's Appendix A).
 */
public interface BadgeWriteService {

    /** Evaluates every real-logic badge and persists a UserBadge row the first time each becomes earned. */
    void syncEarnedBadges(UUID userId);

    // -- global configuration: admin CRUD over badge definitions --------

    BadgeDefinitionAdminResponse createDefinition(CreateBadgeDefinitionRequest request);

    BadgeDefinitionAdminResponse updateDefinition(String id, UpdateBadgeDefinitionRequest request);

    void deleteDefinition(String id);
}
