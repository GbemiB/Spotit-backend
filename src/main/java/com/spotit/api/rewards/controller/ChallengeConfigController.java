package com.spotit.api.rewards.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateChallengeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateChallengeDefinitionRequest;
import com.spotit.api.rewards.service.ChallengeReadService;
import com.spotit.api.rewards.service.ChallengeWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Global-configuration admin surface for weekly-challenge definitions. */
@Tag(name = "Challenge Config (Admin)", description = "Admin CRUD surface for weekly-challenge definitions. No auth check is currently enforced on these endpoints.")
@RestController
@RequestMapping("/api/v1/config/challenges")
@RequiredArgsConstructor
public class ChallengeConfigController {

    private final ChallengeReadService challengeReadService;
    private final ChallengeWriteService challengeWriteService;

    @Operation(summary = ChallengeConfigControllerSwagger.LIST_SUMMARY, description = ChallengeConfigControllerSwagger.LIST_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All challenge definitions.", content = @Content(
                    schema = @Schema(implementation = ChallengeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.LIST_200_EXAMPLE)))
    })
    @GetMapping
    public List<ChallengeDefinitionAdminResponse> list() {
        return challengeReadService.listDefinitionsForAdmin();
    }

    @Operation(summary = ChallengeConfigControllerSwagger.GET_SUMMARY, description = ChallengeConfigControllerSwagger.GET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Challenge definition found.", content = @Content(
                    schema = @Schema(implementation = ChallengeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.GET_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Challenge definition not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @GetMapping("/{id}")
    public ChallengeDefinitionAdminResponse get(@Parameter(description = "Challenge definition id", example = "log_5_days") @PathVariable String id) {
        return challengeReadService.getDefinitionForAdmin(id);
    }

    @Operation(summary = ChallengeConfigControllerSwagger.CREATE_SUMMARY, description = ChallengeConfigControllerSwagger.CREATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = CreateChallengeDefinitionRequest.class),
            examples = @ExampleObject(value = ChallengeConfigControllerSwagger.CREATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Challenge definition created.", content = @Content(
                    schema = @Schema(implementation = ChallengeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.CREATE_201_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "A challenge with this id already exists.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.CREATE_409_EXAMPLE)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChallengeDefinitionAdminResponse create(@Valid @RequestBody CreateChallengeDefinitionRequest request) {
        return challengeWriteService.createDefinition(request);
    }

    @Operation(summary = ChallengeConfigControllerSwagger.UPDATE_SUMMARY, description = ChallengeConfigControllerSwagger.UPDATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = UpdateChallengeDefinitionRequest.class),
            examples = @ExampleObject(value = ChallengeConfigControllerSwagger.UPDATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Challenge definition updated.", content = @Content(
                    schema = @Schema(implementation = ChallengeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.UPDATE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Challenge definition not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @PatchMapping("/{id}")
    public ChallengeDefinitionAdminResponse update(@Parameter(description = "Challenge definition id", example = "log_5_days") @PathVariable String id,
                                                    @RequestBody UpdateChallengeDefinitionRequest request) {
        return challengeWriteService.updateDefinition(id, request);
    }

    @Operation(summary = ChallengeConfigControllerSwagger.DELETE_SUMMARY, description = ChallengeConfigControllerSwagger.DELETE_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Challenge definition deleted.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.DELETE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Challenge definition not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ChallengeConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @DeleteMapping("/{id}")
    public MessageResponse delete(@Parameter(description = "Challenge definition id", example = "log_5_days") @PathVariable String id) {
        challengeWriteService.deleteDefinition(id);
        return new MessageResponse("Challenge definition deleted.");
    }
}
