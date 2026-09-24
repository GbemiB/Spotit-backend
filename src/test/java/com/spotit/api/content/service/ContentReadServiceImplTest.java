package com.spotit.api.content.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.content.dto.ContentFeedResponse;
import com.spotit.api.content.dto.ContentItemAdminResponse;
import com.spotit.api.content.entity.ContentItem;
import com.spotit.api.content.repository.ContentItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentReadServiceImplTest {
    @Mock ContentItemRepository contentItemRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks ContentReadServiceImpl service;

    UUID id = UUID.randomUUID();

    private ContentItem item() {
        return ContentItem.builder().id(id).tag("Health").title("Cycle 101").body("Body").imageUrl("https://img")
                .imageKey("lifestyle").sponsored(true).advertiser("Acme").sortOrder(3).active(false).build();
    }

    @Test
    void feedUsesTheRequestedLimit() {
        when(contentItemRepository.findByActiveTrueOrderBySortOrderAsc(PageRequest.of(0, 5))).thenReturn(List.of(item()));

        ContentFeedResponse feed = service.getFeed(5);

        assertThat(feed.items()).singleElement().satisfies(i -> {
            assertThat(i.id()).isEqualTo(id.toString());
            assertThat(i.title()).isEqualTo("Cycle 101");
            assertThat(i.sponsored()).isTrue();
            assertThat(i.advertiser()).isEqualTo("Acme");
        });
        verifyNoInteractions(configurationDomainService);
    }

    @Test
    void feedFallsBackToTheConfiguredDefaultLimit() {
        when(configurationDomainService.getContentFeedDefaultLimit()).thenReturn(10);
        when(contentItemRepository.findByActiveTrueOrderBySortOrderAsc(PageRequest.of(0, 10))).thenReturn(List.of());

        assertThat(service.getFeed(null).items()).isEmpty();
    }

    @Test
    void adminListIncludesEveryField() {
        when(contentItemRepository.findAll()).thenReturn(List.of(item()));

        assertThat(service.listAllForAdmin()).containsExactly(
                new ContentItemAdminResponse(id, "Health", "Cycle 101", "Body", "https://img", "lifestyle", true, "Acme", 3, false));
    }

    @Test
    void adminGetReturnsTheItem() {
        when(contentItemRepository.findById(id)).thenReturn(Optional.of(item()));

        assertThat(service.getForAdmin(id).title()).isEqualTo("Cycle 101");
    }

    @Test
    void adminGetForAnUnknownIdIs404() {
        when(contentItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getForAdmin(id))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }
}
