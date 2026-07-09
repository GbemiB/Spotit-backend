package com.spotit.api.content.controller;

import com.spotit.api.content.dto.ContentFeedResponse;
import com.spotit.api.content.service.ContentReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentReadService contentReadService;

    @GetMapping("/feed")
    public ContentFeedResponse feed(@RequestParam(required = false) Integer limit) {
        return contentReadService.getFeed(limit);
    }
}
