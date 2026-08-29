package com.spotit.api.rewards.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.rewards.dto.CreateLevelDefinitionRequest;
import com.spotit.api.rewards.dto.LevelDefinitionAdminResponse;
import com.spotit.api.rewards.dto.UpdateLevelDefinitionRequest;
import com.spotit.api.rewards.service.LevelDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Level Config (Admin)", description = "Admin CRUD surface for SpotPoints level tiers. No auth check is currently enforced beyond being signed in.")
@RestController
@RequestMapping("/api/v1/config/levels")
@RequiredArgsConstructor
public class LevelConfigController {
    private final LevelDefinitionService levelDefinitionService;

    @Operation(summary = "List level tiers", description = "All level tiers, in progression order.")
    @GetMapping
    public List<LevelDefinitionAdminResponse> list() {
        return levelDefinitionService.listDefinitionsForAdmin();
    }

    @Operation(summary = "Get a level tier", description = "A single level tier by id.")
    @GetMapping("/{id}")
    public LevelDefinitionAdminResponse get(@Parameter(description = "Level definition id", example = "petal") @PathVariable String id) {
        return levelDefinitionService.getDefinitionForAdmin(id);
    }

    @Operation(summary = "Create a level tier", description = "Adds a new level tier.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelDefinitionAdminResponse create(@Valid @RequestBody CreateLevelDefinitionRequest request) {
        return levelDefinitionService.createDefinition(request);
    }

    @Operation(summary = "Update a level tier", description = "Partial update — omitted fields are left unchanged.")
    @PatchMapping("/{id}")
    public LevelDefinitionAdminResponse update(@Parameter(description = "Level definition id", example = "petal") @PathVariable String id,
                                                @RequestBody UpdateLevelDefinitionRequest request) {
        return levelDefinitionService.updateDefinition(id, request);
    }

    @Operation(summary = "Delete a level tier", description = "Removes a level tier.")
    @DeleteMapping("/{id}")
    public MessageResponse delete(@Parameter(description = "Level definition id", example = "petal") @PathVariable String id) {
        levelDefinitionService.deleteDefinition(id);
        return new MessageResponse("Level definition deleted.");
    }
}
