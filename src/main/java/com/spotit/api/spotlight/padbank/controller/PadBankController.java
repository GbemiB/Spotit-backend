package com.spotit.api.spotlight.padbank.controller;

import com.spotit.api.common.security.CurrentUserId;
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

    @Operation(summary = "Open the bank for a day", description = "Charges the challenge's SP cost and returns the day's visitors (clues only), "
            + "the packs on the shelf with their stock, the bank budget, hearts, delivery cost and the budget needed for the third star.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs")
    public PadBankStartResponse start(@CurrentUserId UUID userId, @RequestBody(required = false) PadBankStartRequest request) {
        return padBankService.start(userId, request);
    }

    @Operation(summary = "Serve the current visitor", description = "Gives the chosen pack by pickup or delivery. result is great (right pack and "
            + "route), ok (right pack only) or oops (wrong pack, costs a heart), with a tip. finished is true after the last visitor or the last "
            + "heart, with stars (0-3) and the three goals.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/runs/{runId}/serve")
    public PadBankServeResponse serve(@CurrentUserId UUID userId, @PathVariable UUID runId, @Valid @RequestBody PadBankServeRequest request) {
        return padBankService.serve(userId, runId, request);
    }
}
