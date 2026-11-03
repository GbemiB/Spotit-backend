package com.spotit.api.spotlight.padbank.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.spotlight.padbank.dto.PadBankCloseResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankDayRules;
import com.spotit.api.spotlight.padbank.dto.PadBankServeRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankServeResponse;
import com.spotit.api.spotlight.padbank.dto.PadBankStartRequest;
import com.spotit.api.spotlight.padbank.dto.PadBankStartResponse;
import com.spotit.api.spotlight.padbank.service.PadBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Pad Bank Run", description = "The Spotlight game where players hand the right period pack to each visitor. Pretend only: no real "
        + "products change hands. Answers are checked on the server.")
@RestController
@RequestMapping("/api/v1/padbank")
@RequiredArgsConstructor
public class PadBankController {
    private final PadBankService padBankService;

    @Operation(summary = "Rules for the next day", description = "The day that would open next, how many visitors it has, the hearts, how many "
            + "visitors are free, the SP fee per visitor after that and the SP delivery cost. Free to call: nothing is charged.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/rules")
    public PadBankDayRules rules(@CurrentUserId UUID userId) {
        return padBankService.rules(userId);
    }

    @Operation(summary = "Open the bank for a day", description = "Charges the challenge's SP cost (0 means free to open) and returns the day's "
            + "visitors (clues only), the packs with their SP price and stock, hearts, free visitors, visitor fee and delivery cost.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs")
    public PadBankStartResponse start(@CurrentUserId UUID userId, @RequestBody(required = false) PadBankStartRequest request) {
        return padBankService.start(userId, request);
    }

    @Operation(summary = "Serve the current visitor", description = "Gives the chosen pack by pickup or delivery. The first free visitors cost "
            + "nothing; after that the visitor fee, the pack price and any delivery are taken from the player's SP (refused if short). result "
            + "is great, ok or oops (wrong pack, costs a heart). finished is true after the last visitor or heart, with stars (0-3) and the "
            + "goals: everyone served, right pack for everyone, right way for everyone.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/serve")
    public PadBankServeResponse serve(@CurrentUserId UUID userId, @PathVariable UUID runId, @Valid @RequestBody PadBankServeRequest request) {
        return padBankService.serve(userId, runId, request);
    }

    @Operation(summary = "Close the bank for today", description = "Ends the day early, for example when the player does not want to pay for "
            + "more visitors. Returns the stars and goals for the visitors served so far.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/close")
    public PadBankCloseResponse close(@CurrentUserId UUID userId, @PathVariable UUID runId) {
        return padBankService.close(userId, runId);
    }
}
