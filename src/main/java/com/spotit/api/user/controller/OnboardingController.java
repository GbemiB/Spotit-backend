package com.spotit.api.user.controller;

import com.spotit.api.common.i18n.EnumMessages;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.user.dto.GoalOption;
import com.spotit.api.user.dto.OnboardingRequest;
import com.spotit.api.user.dto.OnboardingResponse;
import com.spotit.api.user.dto.OnboardingTemplateResponse;
import com.spotit.api.user.entity.Goal;
import com.spotit.api.user.service.UserWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

@Tag(name = "Onboarding", description = "First-run onboarding flow that sets a new user's cycle basics.")
@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class OnboardingController {
    private final UserWriteService userWriteService;

    @Operation(summary = OnboardingControllerSwagger.TEMPLATE_SUMMARY, description = OnboardingControllerSwagger.TEMPLATE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Onboarding template.", content = @Content(
                    schema = @Schema(implementation = OnboardingTemplateResponse.class),
                    examples = @ExampleObject(value = OnboardingControllerSwagger.TEMPLATE_200_EXAMPLE)))
    })
    @GetMapping("/template")
    public OnboardingTemplateResponse template() {
        var goals = Arrays.stream(Goal.values())
                .map(g -> new GoalOption(g.name(), EnumMessages.resolve(g.getCode()), EnumMessages.resolve(g.getDescriptionCode())))
                .toList();
        return new OnboardingTemplateResponse(goals);
    }

    @Operation(summary = OnboardingControllerSwagger.COMPLETE_SUMMARY, description = OnboardingControllerSwagger.COMPLETE_DESCRIPTION,
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = OnboardingRequest.class),
            examples = @ExampleObject(value = OnboardingControllerSwagger.COMPLETE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Onboarding completed.", content = @Content(
                    schema = @Schema(implementation = OnboardingResponse.class),
                    examples = @ExampleObject(value = OnboardingControllerSwagger.COMPLETE_200_EXAMPLE)))
    })
    @PostMapping("/complete")
    public OnboardingResponse complete(@CurrentUserId UUID userId, @Valid @RequestBody OnboardingRequest request) {
        return userWriteService.completeOnboarding(userId, request);
    }
}
