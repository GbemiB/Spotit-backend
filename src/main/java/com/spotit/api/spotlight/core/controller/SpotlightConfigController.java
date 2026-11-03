package com.spotit.api.spotlight.core.controller;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.core.dto.SpotlightChallengeAdmin;
import com.spotit.api.spotlight.core.dto.SpotlightRankAdmin;
import com.spotit.api.spotlight.core.service.SpotlightAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Spotlight Config (Admin)", description = "The challenges on the Spotlight hub (the quiz and the games) and the ranks earned from stars.")
@RestController
@RequestMapping("/api/v1/config/spotlight")
@RequiredArgsConstructor
public class SpotlightConfigController {
    private final SpotlightAdminService spotlightAdminService;

    @Operation(summary = "List Spotlight challenges", description = "The quiz and the games shown on the Spotlight hub, in order.")
    @GetMapping("/challenges")
    public AdminPage<SpotlightChallengeAdmin> challenges(@Parameter(description = "Page number, from 0.", example = "0") @RequestParam(required = false) Integer page,
            @Parameter(description = "Items per page; defaults to the spotlight-admin-page-size setting.", example = "50") @RequestParam(required = false) Integer size) {
        return spotlightAdminService.challenges(page, size);
    }

    @Operation(summary = "Create or update a Spotlight challenge",
            description = "kind is quiz or game; status is live or soon (shown as Coming soon); cost is in SP; minAge is 13 or 18.")
    @PutMapping("/challenges/{id}")
    public SpotlightChallengeAdmin saveChallenge(@Parameter(example = "pad-factory") @PathVariable String id, @RequestBody SpotlightChallengeAdmin body) {
        return spotlightAdminService.saveChallenge(id, body);
    }

    @Operation(summary = "List Spotlight ranks")
    @GetMapping("/ranks")
    public AdminPage<SpotlightRankAdmin> ranks(@Parameter(description = "Page number, from 0.", example = "0") @RequestParam(required = false) Integer page,
            @Parameter(description = "Items per page; defaults to the spotlight-admin-page-size setting.", example = "50") @RequestParam(required = false) Integer size) {
        return spotlightAdminService.ranks(page, size);
    }

    @Operation(summary = "Create or update a Spotlight rank", description = "minStars is the number of stars needed to reach the rank.")
    @PutMapping("/ranks/{id}")
    public SpotlightRankAdmin saveRank(@Parameter(example = "explorer") @PathVariable String id, @RequestBody SpotlightRankAdmin body) {
        return spotlightAdminService.saveRank(id, body);
    }
}
