package com.spotit.api.partner.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.partner.dto.CreateLoveOptionRequest;
import com.spotit.api.partner.dto.LoveOptionAdminResponse;
import com.spotit.api.partner.dto.UpdateLoveOptionRequest;
import com.spotit.api.partner.service.LoveOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Love Options Config (Admin)", description = "Admin CRUD for what a partner can send her: promise options, expressions, "
        + "text starters and the three category titles. Changes reach the apps the next time they load, with no build or deploy.")
@RestController
@RequestMapping("/api/v1/config/love-options")
@RequiredArgsConstructor
public class LoveOptionConfigController {
    private final LoveOptionService loveOptionService;

    @Operation(summary = "List love options", description = "Every option of every kind (promise, expression, starter, category), "
            + "including switched-off ones, ordered by kind then sortOrder.")
    @GetMapping
    public List<LoveOptionAdminResponse> list() {
        return loveOptionService.listForAdmin();
    }

    @Operation(summary = "Create a love option", description = "id is a lower-case code that never changes (e.g. foot_rub). kind is "
            + "promise, expression, starter or category. emoji is shown on the tile (and is the icon for new promises); phrase is used in her "
            + "notification for expressions (e.g. “a hug”). New promises get the next stored number automatically.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoveOptionAdminResponse create(@Valid @RequestBody CreateLoveOptionRequest request) {
        return loveOptionService.create(request);
    }

    @Operation(summary = "Update a love option", description = "Changes label, sub, emoji, phrase, sortOrder or active. "
            + "Only the fields sent are changed.")
    @PatchMapping("/{id}")
    public LoveOptionAdminResponse update(@Parameter(description = "Love option id", example = "snacks") @PathVariable String id,
                                          @Valid @RequestBody UpdateLoveOptionRequest request) {
        return loveOptionService.update(id, request);
    }

    @Operation(summary = "Switch a love option off", description = "Hides it from the partner's pop-up. Past sends that used it keep "
            + "showing correctly in her history, so options are never deleted.")
    @DeleteMapping("/{id}")
    public MessageResponse switchOff(@Parameter(description = "Love option id", example = "snacks") @PathVariable String id) {
        loveOptionService.switchOff(id);
        return new MessageResponse("Love option switched off.");
    }
}
