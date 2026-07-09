package com.spotit.api.content.service;

import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.CreateContentItemRequest;
import com.spotit.api.content.dto.UpdateContentItemRequest;

import java.util.UUID;

/** Global-configuration admin CRUD for the "For you today" content feed — replaces the fixed ReferenceDataSeeder rows. */
public interface ContentWriteService {

    ContentItemAdminResponse create(CreateContentItemRequest request);

    ContentItemAdminResponse update(UUID id, UpdateContentItemRequest request);

    void delete(UUID id);
}
