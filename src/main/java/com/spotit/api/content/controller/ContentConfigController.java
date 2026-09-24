package com.spotit.api.content.controller;

import com.spotit.api.common.dto.ErrorDetail;
import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.CreateContentItemRequest;
import com.spotit.api.content.dto.UpdateContentItemRequest;
import com.spotit.api.content.service.ContentReadService;
import com.spotit.api.content.service.ContentWriteService;
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
import java.util.UUID;

@Tag(name = "Content Config (Admin)", description = "Admin CRUD surface for the content-feed catalog. No auth check is currently enforced on these endpoints.")
@RestController
@RequestMapping("/api/v1/config/content")
@RequiredArgsConstructor
public class ContentConfigController {
    private final ContentReadService contentReadService;
    private final ContentWriteService contentWriteService;

    @Operation(summary = ContentConfigControllerSwagger.LIST_SUMMARY, description = ContentConfigControllerSwagger.LIST_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All content items.", content = @Content(
                    schema = @Schema(implementation = ContentItemAdminResponse.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.LIST_200_EXAMPLE)))
    })
    @GetMapping
    public List<ContentItemAdminResponse> list() {
        return contentReadService.listAllForAdmin();
    }

    @Operation(summary = ContentConfigControllerSwagger.GET_SUMMARY, description = ContentConfigControllerSwagger.GET_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content item found.", content = @Content(
                    schema = @Schema(implementation = ContentItemAdminResponse.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.GET_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Content item not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @GetMapping("/{id}")
    public ContentItemAdminResponse get(@Parameter(description = "Content item id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        return contentReadService.getForAdmin(id);
    }

    @Operation(summary = ContentConfigControllerSwagger.CREATE_SUMMARY, description = ContentConfigControllerSwagger.CREATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = CreateContentItemRequest.class),
            examples = @ExampleObject(value = ContentConfigControllerSwagger.CREATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Content item created.", content = @Content(
                    schema = @Schema(implementation = ContentItemAdminResponse.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.CREATE_201_EXAMPLE)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContentItemAdminResponse create(@Valid @RequestBody CreateContentItemRequest request) {
        return contentWriteService.create(request);
    }

    @Operation(summary = ContentConfigControllerSwagger.UPDATE_SUMMARY, description = ContentConfigControllerSwagger.UPDATE_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = UpdateContentItemRequest.class),
            examples = @ExampleObject(value = ContentConfigControllerSwagger.UPDATE_REQUEST_EXAMPLE)))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content item updated.", content = @Content(
                    schema = @Schema(implementation = ContentItemAdminResponse.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.UPDATE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Content item not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @PatchMapping("/{id}")
    public ContentItemAdminResponse update(@Parameter(description = "Content item id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id,
                                            @RequestBody UpdateContentItemRequest request) {
        return contentWriteService.update(id, request);
    }

    @Operation(summary = ContentConfigControllerSwagger.DELETE_SUMMARY, description = ContentConfigControllerSwagger.DELETE_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content item deleted.", content = @Content(
                    schema = @Schema(implementation = MessageResponse.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.DELETE_200_EXAMPLE))),
            @ApiResponse(responseCode = "404", description = "Content item not found.", content = @Content(
                    schema = @Schema(implementation = ErrorDetail.class),
                    examples = @ExampleObject(value = ContentConfigControllerSwagger.NOT_FOUND_404_EXAMPLE)))
    })
    @DeleteMapping("/{id}")
    public MessageResponse delete(@Parameter(description = "Content item id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        contentWriteService.delete(id);
        return new MessageResponse("Content item deleted.");
    }
}
