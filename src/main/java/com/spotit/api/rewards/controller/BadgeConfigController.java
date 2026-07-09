package com.spotit.api.rewards.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;
import com.spotit.api.rewards.service.BadgeReadService;
import com.spotit.api.rewards.service.BadgeWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Global-configuration admin surface for badge definitions. */
@RestController
@RequestMapping("/api/v1/config/badges")
@RequiredArgsConstructor
public class BadgeConfigController {

    private final BadgeReadService badgeReadService;
    private final BadgeWriteService badgeWriteService;

    @GetMapping
    public List<BadgeDefinitionAdminResponse> list() {
        return badgeReadService.listDefinitionsForAdmin();
    }

    @GetMapping("/{id}")
    public BadgeDefinitionAdminResponse get(@PathVariable String id) {
        return badgeReadService.getDefinitionForAdmin(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BadgeDefinitionAdminResponse create(@Valid @RequestBody CreateBadgeDefinitionRequest request) {
        return badgeWriteService.createDefinition(request);
    }

    @PatchMapping("/{id}")
    public BadgeDefinitionAdminResponse update(@PathVariable String id, @RequestBody UpdateBadgeDefinitionRequest request) {
        return badgeWriteService.updateDefinition(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable String id) {
        badgeWriteService.deleteDefinition(id);
        return new MessageResponse("Badge definition deleted.");
    }
}
