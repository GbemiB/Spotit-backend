package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.ChallengeClaimResponse;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateChallengeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateChallengeDefinitionRequest;

import java.util.UUID;

public interface ChallengeWriteService {

    ChallengeClaimResponse claim(UUID userId, String challengeId);

    // -- global configuration: admin CRUD over challenge definitions ----

    ChallengeDefinitionAdminResponse createDefinition(CreateChallengeDefinitionRequest request);

    ChallengeDefinitionAdminResponse updateDefinition(String id, UpdateChallengeDefinitionRequest request);

    void deleteDefinition(String id);
}
