package com.spotit.api.user.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.user.dto.OnboardingRequest;
import com.spotit.api.user.dto.OnboardingResponse;
import com.spotit.api.user.service.UserWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final UserWriteService userWriteService;

    @PostMapping("/complete")
    public OnboardingResponse complete(@CurrentUserId UUID userId, @Valid @RequestBody OnboardingRequest request) {
        return userWriteService.completeOnboarding(userId, request);
    }
}
