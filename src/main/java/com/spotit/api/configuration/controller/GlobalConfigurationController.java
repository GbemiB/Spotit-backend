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

@Tag(name = "Global Config (Admin)", description = "Admin surface for the single global_configuration table backing all app settings, thresholds, and secrets.")
@RestController
@RequestMapping("/api/v1/config/global")
@RequiredArgsConstructor
public class GlobalConfigurationController {
    private final ConfigurationDomainService configurationDomainService;

    @Operation(summary = "List all properties", description = "Every row in global_configuration, ordered by name. Secret values are redacted.")
    @GetMapping
    public List<GlobalConfigurationResponse> list() {
        return configurationDomainService.listAll();
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
