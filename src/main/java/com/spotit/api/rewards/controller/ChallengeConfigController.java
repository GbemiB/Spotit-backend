package com.spotit.api.rewards.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateChallengeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateChallengeDefinitionRequest;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.ChallengeWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Global-configuration admin surface for weekly-challenge definitions. */
@RestController
@RequestMapping("/api/v1/config/challenges")
@RequiredArgsConstructor
public class ChallengeConfigController {

    private final ChallengeReadService challengeReadService;
    private final ChallengeWriteService challengeWriteService;

    @GetMapping
    public List<ChallengeDefinitionAdminResponse> list() {
        return challengeReadService.listDefinitionsForAdmin();
    }

    @GetMapping("/{id}")
    public ChallengeDefinitionAdminResponse get(@PathVariable String id) {
        return challengeReadService.getDefinitionForAdmin(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChallengeDefinitionAdminResponse create(@Valid @RequestBody CreateChallengeDefinitionRequest request) {
        return challengeWriteService.createDefinition(request);
    }

    @PatchMapping("/{id}")
    public ChallengeDefinitionAdminResponse update(@PathVariable String id, @RequestBody UpdateChallengeDefinitionRequest request) {
        return challengeWriteService.updateDefinition(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        challengeWriteService.deleteDefinition(id);
        return new MessageResponse("Challenge definition deleted.");
    }
}
