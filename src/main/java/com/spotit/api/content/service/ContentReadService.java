package com.spotit.api.content.service;

import com.spotit.api.content.dto.ContentFeedResponse;
import com.spotit.api.content.dto.ContentItemAdminResponse;

import java.util.List;
import java.util.UUID;

public interface ContentReadService {

    ContentFeedResponse getFeed(Integer limit);

    List<ContentItemAdminResponse> listAllForAdmin();

    ContentItemAdminResponse getForAdmin(UUID id);
}
