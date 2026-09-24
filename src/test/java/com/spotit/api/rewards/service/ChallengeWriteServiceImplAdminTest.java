package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.CreateChallengeDefinitionRequest;
import com.spotit.api.rewards.dto.UpdateChallengeDefinitionRequest;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Admin definition management in ChallengeWriteServiceImpl (claim is covered by ChallengeWriteServiceImplTest). */
@ExtendWith(MockitoExtension.class)
class ChallengeWriteServiceImplAdminTest {
    @Mock ChallengeDefinitionRepository challengeDefinitionRepository;
    @Mock UserChallengeProgressRepository progressRepository;
    @Mock ChallengeCalculator calculator;
    @Mock PointsWriteService pointsWriteService;
    @InjectMocks ChallengeWriteServiceImpl service;

    private ChallengeDefinition logFive() {
        return new ChallengeDefinition("log_5_days", "Log 5 days", 50, 5, ChallengeType.WEEKLY_LOG);
    }

    @Test
    void createStoresTheDefinitionWithItsType() {
        when(challengeDefinitionRepository.existsById("streak_7")).thenReturn(false);

        ChallengeDefinitionAdminResponse response = service.createDefinition(
                new CreateChallengeDefinitionRequest("streak_7", "7-day streak", 100, 7, "STATIC"));

        assertThat(response).isEqualTo(new ChallengeDefinitionAdminResponse("streak_7", "7-day streak", 100, 7, "STATIC"));
        verify(challengeDefinitionRepository).save(any(ChallengeDefinition.class));
    }

    @Test
    void createWithAnExistingIdIsRejected() {
        when(challengeDefinitionRepository.existsById("log_5_days")).thenReturn(true);

        assertThatThrownBy(() -> service.createDefinition(new CreateChallengeDefinitionRequest("log_5_days", "X", 1, 1, "STATIC")))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
        verify(challengeDefinitionRepository, never()).save(any());
    }

    @Test
    void updateOnlyChangesTheFieldsThatWereSent() {
        ChallengeDefinition def = logFive();
        when(challengeDefinitionRepository.findById("log_5_days")).thenReturn(Optional.of(def));

        service.updateDefinition("log_5_days", new UpdateChallengeDefinitionRequest(null, 75, null));

        assertThat(def.getReward()).isEqualTo(75);
        assertThat(def.getTitle()).isEqualTo("Log 5 days");
        assertThat(def.getTotal()).isEqualTo(5);
        verify(challengeDefinitionRepository).save(def);
    }

    @Test
    void updateCanChangeEveryField() {
        when(challengeDefinitionRepository.findById("log_5_days")).thenReturn(Optional.of(logFive()));

        assertThat(service.updateDefinition("log_5_days", new UpdateChallengeDefinitionRequest("Log 6 days", 60, 6)))
                .isEqualTo(new ChallengeDefinitionAdminResponse("log_5_days", "Log 6 days", 60, 6, "WEEKLY_LOG"));
    }

    @Test
    void updateForAnUnknownIdIs404() {
        when(challengeDefinitionRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateDefinition("nope", new UpdateChallengeDefinitionRequest("x", null, null)))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void deleteRemovesAnExistingDefinition() {
        when(challengeDefinitionRepository.existsById("log_5_days")).thenReturn(true);

        service.deleteDefinition("log_5_days");

        verify(challengeDefinitionRepository).deleteById("log_5_days");
    }

    @Test
    void deleteForAnUnknownIdIs404() {
        when(challengeDefinitionRepository.existsById("nope")).thenReturn(false);

        assertThatThrownBy(() -> service.deleteDefinition("nope")).isInstanceOf(ApiException.class);
        verify(challengeDefinitionRepository, never()).deleteById(anyString());
    }
}
