package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.log.repository.CycleLogRepository;
import com.spotit.api.rewards.dto.BadgeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateBadgeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateBadgeDefinitionRequest;
import com.spotit.api.rewards.entity.BadgeDefinition;
import com.spotit.api.rewards.entity.UserBadge;
import com.spotit.api.rewards.repository.BadgeDefinitionRepository;
import com.spotit.api.rewards.repository.UserBadgeRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadgeWriteServiceImplTest {
    @Mock UserBadgeRepository userBadgeRepository;
    @Mock BadgeDefinitionRepository badgeDefinitionRepository;
    @Mock CycleLogRepository cycleLogRepository;
    @Mock UserRepository userRepository;
    @Mock ConfigurationDomainService configurationDomainService;
    @InjectMocks BadgeWriteServiceImpl service;

    UUID userId = UUID.randomUUID();

    private void stubThresholds() {
        when(configurationDomainService.getBadgeKnowYourBodyThreshold()).thenReturn(10);
        when(configurationDomainService.getBadgeCycleVeteranThreshold()).thenReturn(90);
        when(configurationDomainService.getBadgeWeekWarriorStreakThreshold()).thenReturn(7);
    }

    private List<String> awardedBadgeIds() {
        ArgumentCaptor<UserBadge> saved = ArgumentCaptor.forClass(UserBadge.class);
        verify(userBadgeRepository, org.mockito.Mockito.atLeast(0)).save(saved.capture());
        return saved.getAllValues().stream().map(UserBadge::getBadgeId).toList();
    }

    @Test
    void syncAwardsEveryBadgeWhoseThresholdIsMet() {
        stubThresholds();
        when(cycleLogRepository.countByUserId(userId)).thenReturn(12L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).longestStreak(8).build()));
        when(userBadgeRepository.existsByUserIdAndBadgeId(eq(userId), anyString())).thenReturn(false);

        service.syncEarnedBadges(userId);

        assertThat(awardedBadgeIds()).containsExactly("first_flow", "know_your_body", "week_warrior");
    }

    @Test
    void syncNeverAwardsTheSameBadgeTwice() {
        stubThresholds();
        when(cycleLogRepository.countByUserId(userId)).thenReturn(1L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).longestStreak(0).build()));
        when(userBadgeRepository.existsByUserIdAndBadgeId(userId, "first_flow")).thenReturn(true);

        service.syncEarnedBadges(userId);

        verify(userBadgeRepository, never()).save(any());
    }

    @Test
    void syncWithNoLogsAndAMissingUserAwardsNothing() {
        stubThresholds();
        when(cycleLogRepository.countByUserId(userId)).thenReturn(0L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        service.syncEarnedBadges(userId);

        verify(userBadgeRepository, never()).save(any());
    }

    @Test
    void createStoresANewDefinition() {
        when(badgeDefinitionRepository.existsById("night_owl")).thenReturn(false);

        BadgeDefinitionAdminResponse response = service.createDefinition(new CreateBadgeDefinitionRequest("night_owl", "Night Owl", "Log late"));

        assertThat(response).isEqualTo(new BadgeDefinitionAdminResponse("night_owl", "Night Owl", "Log late"));
        verify(badgeDefinitionRepository).save(any(BadgeDefinition.class));
    }

    @Test
    void createWithAnExistingIdIsRejected() {
        when(badgeDefinitionRepository.existsById("night_owl")).thenReturn(true);

        assertThatThrownBy(() -> service.createDefinition(new CreateBadgeDefinitionRequest("night_owl", "Night Owl", "Log late")))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
        verify(badgeDefinitionRepository, never()).save(any());
    }

    @Test
    void updateOnlyChangesTheFieldsThatWereSent() {
        BadgeDefinition def = new BadgeDefinition("night_owl", "Night Owl", "Log late");
        when(badgeDefinitionRepository.findById("night_owl")).thenReturn(Optional.of(def));

        service.updateDefinition("night_owl", new UpdateBadgeDefinitionRequest(null, "Log after midnight"));

        assertThat(def.getName()).isEqualTo("Night Owl");
        assertThat(def.getDescription()).isEqualTo("Log after midnight");
        verify(badgeDefinitionRepository, times(1)).save(def);
    }

    @Test
    void updateCanRename() {
        BadgeDefinition def = new BadgeDefinition("night_owl", "Night Owl", "Log late");
        when(badgeDefinitionRepository.findById("night_owl")).thenReturn(Optional.of(def));

        assertThat(service.updateDefinition("night_owl", new UpdateBadgeDefinitionRequest("Owl", null)).name()).isEqualTo("Owl");
    }

    @Test
    void updateForAnUnknownIdIs404() {
        when(badgeDefinitionRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateDefinition("nope", new UpdateBadgeDefinitionRequest("x", null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void deleteRemovesAnExistingDefinition() {
        when(badgeDefinitionRepository.existsById("night_owl")).thenReturn(true);

        service.deleteDefinition("night_owl");

        verify(badgeDefinitionRepository).deleteById("night_owl");
    }

    @Test
    void deleteForAnUnknownIdIs404() {
        when(badgeDefinitionRepository.existsById("nope")).thenReturn(false);

        assertThatThrownBy(() -> service.deleteDefinition("nope")).isInstanceOf(ApiException.class);
        verify(badgeDefinitionRepository, never()).deleteById(anyString());
    }
}
