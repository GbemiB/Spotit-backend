package com.spotit.api.content.controller;

import com.spotit.api.content.dto.ContentFeedResponse;
import com.spotit.api.content.service.ContentReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Content", description = "Public-facing content feed shown in the app.")
@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class ContentController {
    private final ContentReadService contentReadService;

    @Operation(summary = ContentControllerSwagger.FEED_SUMMARY, description = ContentControllerSwagger.FEED_DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content feed.", content = @Content(
                    schema = @Schema(implementation = ContentFeedResponse.class),
                    examples = @ExampleObject(value = ContentControllerSwagger.FEED_200_EXAMPLE)))
    })
    @GetMapping("/feed")
    public ContentFeedResponse feed(@Parameter(description = "Maximum number of items to return; defaults to 10.", example = "10") @RequestParam(required = false) Integer limit) {
        return contentReadService.getFeed(limit);
    }
}
