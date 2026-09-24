package com.spotit.api.rewards.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.rewards.dto.ChallengeDefinitionAdminResponse;
import com.spotit.api.rewards.dto.ChallengeResponse;
import com.spotit.api.rewards.entity.ChallengeDefinition;
import com.spotit.api.rewards.entity.ChallengeType;
import com.spotit.api.rewards.entity.UserChallengeProgress;
import com.spotit.api.rewards.repository.ChallengeDefinitionRepository;
import com.spotit.api.rewards.repository.UserChallengeProgressRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengeReadServiceImplTest {
    @Mock ChallengeDefinitionRepository challengeDefinitionRepository;
    @Mock UserChallengeProgressRepository progressRepository;
    @Mock ChallengeCalculator calculator;
    @InjectMocks ChallengeReadServiceImpl service;

    UUID userId = UUID.randomUUID();
    LocalDate weekStart = LocalDate.of(2026, 9, 21);

    private ChallengeDefinition def(String id, int total) {
        return new ChallengeDefinition(id, "Title " + id, 50, total, ChallengeType.WEEKLY_LOG);
    }

    @Test
    void challengesHideTheDailyLogDefinitionAndCapProgressAtTheTotal() {
        ChallengeDefinition logFive = def("log_5_days", 5);
        ChallengeDefinition streak = def("streak_7", 7);
        when(calculator.currentWeekStart()).thenReturn(weekStart);
        when(challengeDefinitionRepository.findAll()).thenReturn(List.of(def("daily_log", 1), logFive, streak));
        when(calculator.computeDone(userId, logFive)).thenReturn(6);
        when(calculator.computeDone(userId, streak)).thenReturn(3);
        when(progressRepository.findByUserIdAndChallengeIdAndWeekStartDate(userId, "log_5_days", weekStart))
                .thenReturn(Optional.of(UserChallengeProgress.builder().claimed(true).build()));
        when(progressRepository.findByUserIdAndChallengeIdAndWeekStartDate(userId, "streak_7", weekStart)).thenReturn(Optional.empty());

        List<ChallengeResponse> challenges = service.getChallenges(userId);

        assertThat(challenges).containsExactly(
                new ChallengeResponse("log_5_days", "Title log_5_days", 50, 5, 5, true, true),
                new ChallengeResponse("streak_7", "Title streak_7", 50, 3, 7, false, false));
    }

    @Test
    void dailyLogRewardComesFromItsDefinition() {
        when(challengeDefinitionRepository.findById("daily_log")).thenReturn(Optional.of(new ChallengeDefinition("daily_log", "Daily", 10, 1,
                ChallengeType.STATIC)));

        assertThat(service.getDailyLogReward()).isEqualTo(10);
    }

    @Test
    void dailyLogRewardWithoutADefinitionIs404() {
        when(challengeDefinitionRepository.findById("daily_log")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDailyLogReward())
                .isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void adminListIncludesTheType() {
        when(challengeDefinitionRepository.findAll()).thenReturn(List.of(def("log_5_days", 5)));

        assertThat(service.listDefinitionsForAdmin())
                .containsExactly(new ChallengeDefinitionAdminResponse("log_5_days", "Title log_5_days", 50, 5, "WEEKLY_LOG"));
    }

    @Test
    void adminGetReturnsOneDefinition() {
        when(challengeDefinitionRepository.findById("log_5_days")).thenReturn(Optional.of(def("log_5_days", 5)));

        assertThat(service.getDefinitionForAdmin("log_5_days").total()).isEqualTo(5);
    }

    @Test
    void adminGetForAnUnknownIdIs404() {
        when(challengeDefinitionRepository.findById("nope")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDefinitionForAdmin("nope")).isInstanceOf(ApiException.class);
    }
}
