package com.spotit.api.content.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.dto.CreateContentItemRequest;
import com.spotit.api.content.dto.UpdateContentItemRequest;
import com.spotit.api.content.entity.ContentItem;
import com.spotit.api.content.repository.ContentItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentWriteServiceImplTest {
    @Mock ContentItemRepository contentItemRepository;
    @InjectMocks ContentWriteServiceImpl service;

    UUID id = UUID.randomUUID();

    private ContentItem existing() {
        return ContentItem.builder().id(id).tag("Health").title("Old").body("Old body").imageKey("food")
                .sponsored(false).sortOrder(1).active(true).build();
    }

    @Test
    void createDefaultsToActiveWhenNotSpecified() {
        ContentItemAdminResponse response = service.create(
                new CreateContentItemRequest("Health", "Cycle 101", "Body", null, "lifestyle", true, "Acme", 4, null));

        ArgumentCaptor<ContentItem> saved = ArgumentCaptor.forClass(ContentItem.class);
        verify(contentItemRepository).save(saved.capture());
        assertThat(saved.getValue().isActive()).isTrue();
        assertThat(saved.getValue().getAdvertiser()).isEqualTo("Acme");
        assertThat(response.sortOrder()).isEqualTo(4);
        assertThat(response.active()).isTrue();
    }

    @Test
    void createCanStartInactive() {
        ContentItemAdminResponse response = service.create(
                new CreateContentItemRequest("Health", "Draft", "Body", null, null, false, null, 0, false));

        assertThat(response.active()).isFalse();
    }

    @Test
    void updateOnlyChangesTheFieldsThatWereSent() {
        ContentItem item = existing();
        when(contentItemRepository.findById(id)).thenReturn(Optional.of(item));

        service.update(id, new UpdateContentItemRequest(null, "New", null, null, null, null, null, 9, null));

        assertThat(item.getTitle()).isEqualTo("New");
        assertThat(item.getSortOrder()).isEqualTo(9);
        assertThat(item.getTag()).isEqualTo("Health");
        assertThat(item.getBody()).isEqualTo("Old body");
        assertThat(item.isActive()).isTrue();
        verify(contentItemRepository).save(item);
    }

    @Test
    void updateCanChangeEveryField() {
        ContentItem item = existing();
        when(contentItemRepository.findById(id)).thenReturn(Optional.of(item));

        ContentItemAdminResponse response = service.update(id,
                new UpdateContentItemRequest("Nutrition", "T", "New body", "https://img", "lifestyle", true, "Acme", 2, false));

        assertThat(response).isEqualTo(
                new ContentItemAdminResponse(id, "Nutrition", "T", "New body", "https://img", "lifestyle", true, "Acme", 2, false));
    }

    @Test
    void updateIgnoresABlankBody() {
        ContentItem item = existing();
        when(contentItemRepository.findById(id)).thenReturn(Optional.of(item));

        service.update(id, new UpdateContentItemRequest(null, null, "  ", null, null, null, null, null, null));

        assertThat(item.getBody()).isEqualTo("Old body");
    }

    @Test
    void updateForAnUnknownIdIs404() {
        when(contentItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, new UpdateContentItemRequest(null, null, null, null, null, null, null, null, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void deleteRemovesAnExistingItem() {
        when(contentItemRepository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(contentItemRepository).deleteById(id);
    }

    @Test
    void deleteForAnUnknownIdIs404() {
        when(contentItemRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id)).isInstanceOf(ApiException.class);
        verify(contentItemRepository, never()).deleteById(id);
    }
}
