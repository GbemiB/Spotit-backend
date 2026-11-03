package com.spotit.api.spotlight.core.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.spotlight.core.dto.SpotlightHomeResponse;
import com.spotit.api.spotlight.core.service.SpotlightHomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Spotlight", description = "The Spotlight hub: the player's level, wins and week across every challenge, and the challenges on offer.")
@RestController
@RequestMapping("/api/v1/spotlight")
@RequiredArgsConstructor
public class SpotlightController {
    private final SpotlightHomeService spotlightHomeService;

    @Operation(summary = "Spotlight hub", description = "SpotPoints balance; stats summed over every challenge (streak, stars, levels won, "
            + "right answers, best run, topics started, the last 7 days and the rank); the challenges with cost, status and age lock; "
            + "and the Home invite settings.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/home")
    public SpotlightHomeResponse home(@CurrentUserId UUID userId) {
        return spotlightHomeService.home(userId);
    }
}
