package com.spotit.api.rewards.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;
import com.spotit.api.rewards.service.BadgeReadService;
import com.spotit.api.rewards.service.BadgeWriteService;
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

@Tag(name = "Badge Config (Admin)", description = "Admin CRUD surface for badge definitions. No auth check is currently enforced on these endpoints.")
@RestController
@RequestMapping("/api/v1/config/badges")
@RequiredArgsConstructor
public class BadgeConfigController {
    private final BadgeReadService badgeReadService;
    private final BadgeWriteService badgeWriteService;

    @Operation(summary = BadgeConfigControllerSwagger.LIST_SUMMARY, description = BadgeConfigControllerSwagger.LIST_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All badge definitions.", content = @Content(
                    schema = @Schema(implementation = BadgeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.LIST_200_EXAMPLE)))
    })
    @GetMapping
    public List<BadgeDefinitionAdminResponse> list() {
        return badgeReadService.listDefinitionsForAdmin();
    }

    @Operation(summary = BadgeConfigControllerSwagger.GET_SUMMARY, description = BadgeConfigControllerSwagger.GET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Badge definition found.", content = @Content(
                    schema = @Schema(implementation = BadgeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.GET_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Badge definition not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @GetMapping("/{id}")
    public BadgeDefinitionAdminResponse get(@Parameter(description = "Badge definition id", example = "first_flow") @PathVariable String id) {
        return badgeReadService.getDefinitionForAdmin(id);
    }

    @Operation(summary = BadgeConfigControllerSwagger.CREATE_SUMMARY, description = BadgeConfigControllerSwagger.CREATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = CreateBadgeDefinitionRequest.class),
            examples = @ExampleObject(value = BadgeConfigControllerSwagger.CREATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Badge definition created.", content = @Content(
                    schema = @Schema(implementation = BadgeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.CREATE_201_EXAMPLE))),
            @ApiResponse(responseCode = "409", description = "A badge with this id already exists.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.CREATE_409_EXAMPLE)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BadgeDefinitionAdminResponse create(@Valid @RequestBody CreateBadgeDefinitionRequest request) {
        return badgeWriteService.createDefinition(request);
    }

    @Operation(summary = BadgeConfigControllerSwagger.UPDATE_SUMMARY, description = BadgeConfigControllerSwagger.UPDATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = UpdateBadgeDefinitionRequest.class),
            examples = @ExampleObject(value = BadgeConfigControllerSwagger.UPDATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Badge definition updated.", content = @Content(
                    schema = @Schema(implementation = BadgeDefinitionAdminResponse.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.UPDATE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Badge definition not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @PatchMapping("/{id}")
    public BadgeDefinitionAdminResponse update(@Parameter(description = "Badge definition id", example = "night_owl") @PathVariable String id,
                                                @RequestBody UpdateBadgeDefinitionRequest request) {
        return badgeWriteService.updateDefinition(id, request);
    }

    @Operation(summary = BadgeConfigControllerSwagger.DELETE_SUMMARY, description = BadgeConfigControllerSwagger.DELETE_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Badge definition deleted.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.DELETE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Badge definition not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = BadgeConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @DeleteMapping("/{id}")
    public MessageResponse delete(@Parameter(description = "Badge definition id", example = "night_owl") @PathVariable String id) {
        badgeWriteService.deleteDefinition(id);
        return new MessageResponse("Badge definition deleted.");
    }
}
