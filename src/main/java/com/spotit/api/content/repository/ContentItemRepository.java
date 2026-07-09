package com.spotit.api.content.repository;

import com.spotit.api.content.entity.ContentItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentItemRepository extends JpaRepository<ContentItem, java.util.UUID> {

    List<ContentItem> findAllByOrderBySortOrderAsc(Pageable pageable);
}
