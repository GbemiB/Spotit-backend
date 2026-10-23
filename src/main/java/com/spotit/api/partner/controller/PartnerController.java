package com.spotit.api.partner.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.partner.dto.AcceptInviteRequest;
import com.spotit.api.partner.dto.InviteRequest;
import com.spotit.api.partner.dto.PartnerCheckInRequest;
import com.spotit.api.partner.dto.PartnerCheckInResponse;
import com.spotit.api.partner.dto.PartnerProfileResponse;
import com.spotit.api.partner.dto.PartnerSettingsRequest;
import com.spotit.api.partner.dto.PartnerStatusResponse;
import com.spotit.api.partner.service.PartnerCheckInService;
import com.spotit.api.partner.service.PartnerReadService;
import com.spotit.api.partner.service.PartnerWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Partner", description = "Invite a partner who sees her Home, Calendar, Insights and daily logs read-only, and earns his own points.")
@RestController
@RequestMapping("/api/v1/partner")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerWriteService partnerWriteService;
    private final PartnerReadService partnerReadService;
    private final PartnerCheckInService partnerCheckInService;

    @Operation(summary = PartnerControllerSwagger.INVITE_SUMMARY, description = PartnerControllerSwagger.INVITE_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = InviteRequest.class),
            examples = @ExampleObject(value = PartnerControllerSwagger.INVITE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invite sent.", content = @Content(
                    schema = @Schema(implementation = PartnerStatusResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.STATUS_PENDING_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "Already linked, or the email cannot be a partner.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.INVITE_409_EXAMPLE))),
            @ApiResponse(responseCode = "429", description = "Too many invites in 24 hours.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.INVITE_429_EXAMPLE)))
    })
    @PostMapping("/invite")
    public PartnerStatusResponse invite(@CurrentUserId UUID userId, @Valid @RequestBody InviteRequest request) {
        return partnerWriteService.invite(userId, request);
    }

    @Operation(summary = PartnerControllerSwagger.RESEND_SUMMARY, description = PartnerControllerSwagger.RESEND_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invite sent again.", content = @Content(
                    schema = @Schema(implementation = PartnerStatusResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.STATUS_PENDING_EXAMPLE)))
    })
    @PostMapping("/invite/resend")
    public PartnerStatusResponse resendInvite(@CurrentUserId UUID userId) {
        return partnerWriteService.resendInvite(userId);
    }

    @Operation(summary = PartnerControllerSwagger.CANCEL_SUMMARY, description = PartnerControllerSwagger.CANCEL_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invite cancelled.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.CANCEL_200_EXAMPLE)))
    })
    @DeleteMapping("/invite")
    public MessageResponse cancelInvite(@CurrentUserId UUID userId) {
        partnerWriteService.cancelInvite(userId);
        return new MessageResponse("Invite cancelled.");
    }

    @Operation(summary = PartnerControllerSwagger.ACCEPT_SUMMARY, description = PartnerControllerSwagger.ACCEPT_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = AcceptInviteRequest.class),
            examples = @ExampleObject(value = PartnerControllerSwagger.ACCEPT_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Linked as partner.", content = @Content(
                    schema = @Schema(implementation = PartnerStatusResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.STATUS_PARTNER_EXAMPLE))),
            @ApiResponse(responseCode = "400", description = "Wrong code.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.ACCEPT_400_EXAMPLE))),
            @ApiResponse(responseCode = "410", description = "Invite expired.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.ACCEPT_410_EXAMPLE)))
    })
    @PostMapping("/accept")
    public PartnerStatusResponse accept(@CurrentUserId UUID userId, @Valid @RequestBody AcceptInviteRequest request) {
        return partnerWriteService.accept(userId, request);
    }

    @Operation(summary = PartnerControllerSwagger.STATUS_SUMMARY, description = PartnerControllerSwagger.STATUS_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current sharing status.", content = @Content(
                    schema = @Schema(implementation = PartnerStatusResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.STATUS_PENDING_EXAMPLE)))
    })
    @GetMapping("/status")
    public PartnerStatusResponse status(@CurrentUserId UUID userId) {
        return partnerReadService.getStatus(userId);
    }

    @Operation(summary = PartnerControllerSwagger.SETTINGS_SUMMARY, description = PartnerControllerSwagger.SETTINGS_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = PartnerSettingsRequest.class),
            examples = @ExampleObject(value = PartnerControllerSwagger.SETTINGS_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Settings saved.", content = @Content(
                    schema = @Schema(implementation = PartnerStatusResponse.class)))
    })
    @PatchMapping("/settings")
    public PartnerStatusResponse updateSettings(@CurrentUserId UUID userId, @Valid @RequestBody PartnerSettingsRequest request) {
        return partnerWriteService.updateSettings(userId, request);
    }

    @Operation(summary = PartnerControllerSwagger.UNLINK_SUMMARY, description = PartnerControllerSwagger.UNLINK_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sharing stopped.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.UNLINK_200_EXAMPLE)))
    })
    @DeleteMapping
    public MessageResponse unlink(@CurrentUserId UUID userId) {
        partnerWriteService.unlink(userId);
        return new MessageResponse("Sharing stopped.");
    }

    @Operation(summary = PartnerControllerSwagger.PROFILE_SUMMARY, description = PartnerControllerSwagger.PROFILE_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Her name and cycle settings.", content = @Content(
                    schema = @Schema(implementation = PartnerProfileResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.PROFILE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Not a linked partner.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.PARTNER_404_EXAMPLE)))
    })
    @GetMapping("/profile")
    public PartnerProfileResponse profile(@CurrentUserId UUID userId) {
        return partnerReadService.getOwnerProfile(userId);
    }

    @Operation(summary = PartnerControllerSwagger.CHECK_IN_GET_SUMMARY, description = PartnerControllerSwagger.CHECK_IN_GET_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Today's check-in state.", content = @Content(
                    schema = @Schema(implementation = PartnerCheckInResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.CHECK_IN_200_EXAMPLE)))
    })
    @GetMapping("/check-in")
    public PartnerCheckInResponse checkInToday(@CurrentUserId UUID userId) {
        return partnerCheckInService.getToday(userId);
    }

    @Operation(summary = PartnerControllerSwagger.CHECK_IN_POST_SUMMARY, description = PartnerControllerSwagger.CHECK_IN_POST_DESCRIPTION,
            security = @SecurityRequirement(name = "bearerAuth"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = PartnerCheckInRequest.class),
            examples = @ExampleObject(value = PartnerControllerSwagger.CHECK_IN_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Checked in; points awarded.", content = @Content(
                    schema = @Schema(implementation = PartnerCheckInResponse.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.CHECK_IN_200_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "Already checked in today.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = PartnerControllerSwagger.CHECK_IN_409_EXAMPLE)))
    })
    @PostMapping("/check-in")
    public PartnerCheckInResponse checkIn(@CurrentUserId UUID userId, @Valid @RequestBody PartnerCheckInRequest request) {
        return partnerCheckInService.checkIn(userId, request);
    }
}
