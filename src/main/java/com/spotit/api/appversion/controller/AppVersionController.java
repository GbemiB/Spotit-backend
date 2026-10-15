package com.spotit.api.appversion.controller;

import com.spotit.api.appversion.dto.AppVersionPolicyResponse;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "App version", description = "Minimum app builds; older builds are asked to update before they can continue.")
@RestController
@RequestMapping("/api/v1/app")
@RequiredArgsConstructor
public class AppVersionController {
    private final ConfigurationDomainService configurationDomainService;

    @Operation(summary = "Get the app version policy", description = "Public. The lowest build number allowed on each platform "
            + "(0 means no minimum) and the store page the update screen opens. Set via the app-* properties in the admin config.")
    @GetMapping("/version-policy")
    public AppVersionPolicyResponse versionPolicy() {
        return new AppVersionPolicyResponse(
                new AppVersionPolicyResponse.Platform(configurationDomainService.getAppMinBuildAndroid(),
                        configurationDomainService.getAppStoreUrlAndroid()),
                new AppVersionPolicyResponse.Platform(configurationDomainService.getAppMinBuildIos(),
                        configurationDomainService.getAppStoreUrlIos()));
    }
}
