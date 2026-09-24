package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.rewards.LevelUtil;
import com.spotit.api.rewards.dto.PointsHistoryEntryResponse;
import com.spotit.api.rewards.dto.PointsHistoryPageResponse;
import com.spotit.api.rewards.dto.RewardsSummaryResponse;
import com.spotit.api.rewards.entity.PointsHistoryEntry;
import com.spotit.api.rewards.repository.PointsHistoryRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardsReadServiceImplTest {
    @Mock UserRepository userRepository;
    @Mock PointsHistoryRepository pointsHistoryRepository;
    @Mock LevelDefinitionService levelDefinitionService;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks RewardsReadServiceImpl service;

    UUID userId = UUID.randomUUID();

    private static String cursor(int offset) {
        return Base64.getEncoder().encodeToString(String.valueOf(offset).getBytes(StandardCharsets.UTF_8));
    }

    private List<PointsHistoryEntry> entries(int count) {
        return Collections.nCopies(count, PointsHistoryEntry.builder().icon("star").label("Daily log").delta(10)
                .occurredOn(LocalDate.of(2026, 9, 20)).build());
    }

    @Test
    void summaryPlacesTheUserOnTheLevelLadder() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).points(620).streak(4).longestStreak(9).build()));
        when(levelDefinitionService.getLevelDefs()).thenReturn(List.of(
                new LevelUtil.LevelDef("Blush", 0, 500), new LevelUtil.LevelDef("Petal", 500, 2000), new LevelUtil.LevelDef("Rosé", 2000, 5000)));

        assertThat(service.getSummary(userId)).isEqualTo(new RewardsSummaryResponse(
                620, "Petal", new RewardsSummaryResponse.LevelRange(500, 2000), "Rosé", 1380L, 4, 9));
    }

    @Test
    void summaryForAnUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getSummary(userId))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void aFullPageHasACursorToTheNextOne() {
        when(pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 2))).thenReturn(entries(2));

        PointsHistoryPageResponse page = service.getHistory(userId, 2, null);

        assertThat(page.entries()).hasSize(2).first()
                .isEqualTo(new PointsHistoryEntryResponse("star", "Daily log", 10, LocalDate.of(2026, 9, 20)));
        assertThat(page.nextCursor()).isEqualTo(cursor(2));
    }

    @Test
    void theCursorSelectsTheNextPage() {
        when(pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(2, 2))).thenReturn(entries(1));

        PointsHistoryPageResponse page = service.getHistory(userId, 2, cursor(4));

        assertThat(page.entries()).hasSize(1);
        assertThat(page.nextCursor()).isNull();
    }

    @Test
    void noLimitUsesTheConfiguredPageSize() {
        when(configurationDomainService.getRewardsHistoryPageSize()).thenReturn(20);
        when(pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 20))).thenReturn(List.of());

        assertThat(service.getHistory(userId, null, " ").entries()).isEmpty();
    }

    @Test
    void aGarbageCursorStartsFromTheBeginning() {
        when(pointsHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 5))).thenReturn(List.of());

        assertThat(service.getHistory(userId, 5, "not-base64!").nextCursor()).isNull();
    }
}
