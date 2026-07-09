package com.spotit.api.content.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.content.dto.ContentFeedResponse;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.ContentItemResponse;
import com.spotit.api.content.entity.ContentItem;
import com.spotit.api.content.repository.ContentItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentReadServiceImpl implements ContentReadService {

    private static final int DEFAULT_LIMIT = 10;

    private final ContentItemRepository contentItemRepository;

    @Override
    @Transactional(readOnly = true)
    public ContentFeedResponse getFeed(Integer limit) {
        int pageSize = limit == null ? DEFAULT_LIMIT : limit;
        var items = contentItemRepository.findAllByOrderBySortOrderAsc(PageRequest.of(0, pageSize)).stream()
                .map(i -> new ContentItemResponse(i.getId().toString(), i.getTag(), i.getTitle(), i.getImageUrl(), i.isSponsored(), i.getAdvertiser()))
                .toList();
        return new ContentFeedResponse(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentItemAdminResponse> listAllForAdmin() {
        return contentItemRepository.findAll().stream().map(this::toAdminResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ContentItemAdminResponse getForAdmin(UUID id) {
        return contentItemRepository.findById(id)
                .map(this::toAdminResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Content item not found."));
    }

    private ContentItemAdminResponse toAdminResponse(ContentItem i) {
        return new ContentItemAdminResponse(i.getId(), i.getTag(), i.getTitle(), i.getImageUrl(), i.isSponsored(), i.getAdvertiser(), i.getSortOrder());
    }
}
