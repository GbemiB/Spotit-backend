package com.spotit.api.configuration.controller;

import com.spotit.api.configuration.dto.GlobalConfigurationResponse;
import com.spotit.api.configuration.dto.UpdateGlobalConfigurationRequest;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Global Config (Admin)", description = "Admin surface for all app settings, thresholds, and secrets — stored as typed columns across security_config, smtp_config, and global_config, exposed here under their flat property names.")
@RestController
@RequestMapping("/api/v1/config/global")
@RequiredArgsConstructor
public class GlobalConfigurationController {
    private final ConfigurationDomainService configurationDomainService;

    @Operation(summary = "List all properties", description = "Every configuration property, ordered by name. Secret values are redacted.")
    @GetMapping
    public List<GlobalConfigurationResponse> list() {
        return configurationDomainService.listAll();
    }

    @Operation(summary = "List all group names", description = "Every distinct group a property belongs to (e.g. security, points, badges, smtp).")
    @GetMapping("/groups")
    public List<String> listGroups() {
        return configurationDomainService.listGroupNames();
    }

    @Operation(summary = "List properties in a group", description = "Every property whose group matches, ordered by name. Secret values are redacted.")
    @GetMapping("/group/{groupName}")
    public List<GlobalConfigurationResponse> listByGroup(@Parameter(description = "Group name", example = "badges") @PathVariable String groupName) {
        return configurationDomainService.listByGroup(groupName);
    }

    @Operation(summary = "Get a property", description = "Fetch a single property by its name (e.g. cycle-default-length, smtp-primary-host).")
    @GetMapping("/{name}")
    public GlobalConfigurationResponse get(@Parameter(description = "Property name", example = "cycle-default-length") @PathVariable String name) {
        return configurationDomainService.getByName(name);
    }

    @Operation(summary = "Update a property", description = "Partial update — only the fields set on the request body are changed. Setting stringValue on a secret-holding property encrypts it before storage.")
    @PatchMapping("/{name}")
    public GlobalConfigurationResponse update(@Parameter(description = "Property name", example = "cycle-default-length") @PathVariable String name,
                                               @Valid @RequestBody UpdateGlobalConfigurationRequest request) {
        return configurationDomainService.update(name, request);
    }
}
