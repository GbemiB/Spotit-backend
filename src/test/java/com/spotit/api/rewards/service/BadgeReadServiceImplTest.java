package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.BadgeResponse;
import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.UserBadge;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadgeReadServiceImplTest {
    @Mock BadgeDefinitionRepository badgeDefinitionRepository;
    @Mock UserBadgeRepository userBadgeRepository;
    @Mock BadgeWriteService badgeWriteService;
    @InjectMocks BadgeReadServiceImpl service;

    UUID userId = UUID.randomUUID();

    @Test
    void badgesSyncFirstThenMarkWhichAreEarned() {
        Instant earnedAt = Instant.parse("2026-09-01T08:00:00Z");
        when(userBadgeRepository.findByUserId(userId)).thenReturn(List.of(new UserBadge(userId, "first_flow", earnedAt)));
        when(badgeDefinitionRepository.findAll()).thenReturn(List.of(
                new BadgeDefinition("first_flow", "First Flow", "Log once"),
                new BadgeDefinition("night_owl", "Night Owl", "Log late")));

        List<BadgeResponse> badges = service.getBadgesSyncingNewlyEarned(userId);

        assertThat(badges).containsExactly(
                new BadgeResponse("first_flow", "First Flow", true, earnedAt),
                new BadgeResponse("night_owl", "Night Owl", false, null));
        InOrder order = inOrder(badgeWriteService, userBadgeRepository);
        order.verify(badgeWriteService).syncEarnedBadges(userId);
        order.verify(userBadgeRepository).findByUserId(userId);
    }

    @Test
    void adminListReturnsEveryDefinition() {
        when(badgeDefinitionRepository.findAll()).thenReturn(List.of(new BadgeDefinition("night_owl", "Night Owl", "Log late")));

        assertThat(service.listDefinitionsForAdmin())
                .containsExactly(new BadgeDefinitionAdminResponse("night_owl", "Night Owl", "Log late"));
    }

    @Test
    void adminGetReturnsOneDefinition() {
        when(badgeDefinitionRepository.findById("night_owl")).thenReturn(Optional.of(new BadgeDefinition("night_owl", "Night Owl", "Log late")));

        assertThat(service.getDefinitionForAdmin("night_owl").name()).isEqualTo("Night Owl");
    }

    @Test
    void adminGetForAnUnknownIdIs404() {
        when(badgeDefinitionRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDefinitionForAdmin("nope"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }
}
