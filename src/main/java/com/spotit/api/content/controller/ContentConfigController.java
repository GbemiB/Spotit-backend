package com.spotit.api.content.controller;

import com.spotit.api.common.dto.MessageResponse;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.CreateContentItemRequest;
import com.spotit.api.content.dto.UpdateContentItemRequest;
import com.spotit.api.content.service.ContentReadService;
import com.spotit.api.content.service.ContentWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/** Global-configuration admin surface for the content feed. */
@RestController
@RequestMapping("/api/v1/config/content")
@RequiredArgsConstructor
public class ContentConfigController {

    private final ContentReadService contentReadService;
    private final ContentWriteService contentWriteService;

    @GetMapping
    public List<ContentItemAdminResponse> list() {
        return contentReadService.listAllForAdmin();
    }

    @GetMapping("/{id}")
    public ContentItemAdminResponse get(@PathVariable UUID id) {
        return contentReadService.getForAdmin(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContentItemAdminResponse create(@Valid @RequestBody CreateContentItemRequest request) {
        return contentWriteService.create(request);
    }

    @PatchMapping("/{id}")
    public ContentItemAdminResponse update(@PathVariable UUID id, @RequestBody UpdateContentItemRequest request) {
        return contentWriteService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable UUID id) {
        contentWriteService.delete(id);
        return new MessageResponse("Content item deleted.");
    }
}
