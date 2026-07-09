package com.spotit.api.content.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.CreateContentItemRequest;
import com.spotit.api.content.dto.UpdateContentItemRequest;
import com.spotit.api.content.entity.ContentItem;
import com.spotit.api.content.repository.ContentItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentWriteServiceImpl implements ContentWriteService {

    private final ContentItemRepository contentItemRepository;

    @Override
    @Transactional
    public ContentItemAdminResponse create(CreateContentItemRequest request) {
        ContentItem item = ContentItem.builder()
                .tag(request.tag())
                .title(request.title())
                .imageUrl(request.imageUrl())
                .sponsored(request.sponsored())
                .advertiser(request.advertiser())
                .sortOrder(request.sortOrder())
                .build();
        contentItemRepository.save(item);
        return toAdminResponse(item);
    }

    @Override
    @Transactional
    public ContentItemAdminResponse update(UUID id, UpdateContentItemRequest request) {
        ContentItem item = contentItemRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "Content item not found."));
        if (request.tag() != null) item.setTag(request.tag());
        if (request.title() != null) item.setTitle(request.title());
        if (request.imageUrl() != null) item.setImageUrl(request.imageUrl());
        if (request.sponsored() != null) item.setSponsored(request.sponsored());
        if (request.advertiser() != null) item.setAdvertiser(request.advertiser());
        if (request.sortOrder() != null) item.setSortOrder(request.sortOrder());
        contentItemRepository.save(item);
        return toAdminResponse(item);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!contentItemRepository.existsById(id)) {
            throw new ApiException(ErrorCode.NOT_FOUND, "Content item not found.");
        }
        contentItemRepository.deleteById(id);
    }

    private ContentItemAdminResponse toAdminResponse(ContentItem i) {
        return new ContentItemAdminResponse(i.getId(), i.getTag(), i.getTitle(), i.getImageUrl(), i.isSponsored(), i.getAdvertiser(), i.getSortOrder());
    }
}
