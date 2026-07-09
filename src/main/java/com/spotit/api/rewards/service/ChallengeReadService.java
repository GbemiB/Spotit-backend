package com.spotit.api.rewards.service;

import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.ChallengeResponse;

import java.util.List;
import java.util.UUID;

public interface ChallengeReadService {

    List<ChallengeResponse> getChallenges(UUID userId);

    List<ChallengeDefinitionAdminResponse> listDefinitionsForAdmin();

    ChallengeDefinitionAdminResponse getDefinitionForAdmin(String id);
}
