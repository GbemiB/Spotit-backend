package com.spotit.api.spotlight.core.service;

import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.spotlight.core.dto.SpotlightHomeResponse;
import com.spotit.api.spotlight.core.dto.SpotlightInvite;
import com.spotit.api.spotlight.core.entity.SpotlightChallenge;
import com.spotit.api.spotlight.core.entity.SpotlightRank;
import com.spotit.api.spotlight.core.repository.SpotlightChallengeRepository;
import com.spotit.api.spotlight.core.repository.SpotlightRankRepository;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpotlightHomeServiceTest {
    final UUID userId = UUID.randomUUID();
    final LocalDate today = LocalDate.of(2026, 11, 2);

    static SpotlightGame game(String id, GameSummary summary) {
        return new SpotlightGame() {
            @Override
            public String challengeId() {
                return id;
            }

            @Override
            public GameSummary summary(UUID userId, LocalDate today) {
                return summary;
            }
        };
    }

    @Test
    void theHubAddsUpEveryGameAndLocksAdultChallengesForTeens() {
        UserRepository users = mock(UserRepository.class);
        UserClock clock = mock(UserClock.class);
        ConfigurationDomainService config = mock(ConfigurationDomainService.class);
        SpotlightChallengeRepository challenges = mock(SpotlightChallengeRepository.class);
        SpotlightRankRepository ranks = mock(SpotlightRankRepository.class);
        User user = User.builder().id(userId).points(240).dob(LocalDate.of(2011, 1, 1)).build();
        when(users.findById(userId)).thenReturn(Optional.of(user));
        when(clock.today(user)).thenReturn(today);
        when(config.isSpotlightInviteEnabled()).thenReturn(true);
        when(config.getSpotlightInviteEveryDays()).thenReturn(3);
        when(config.getSpotlightInviteMaxDismissals()).thenReturn(3);
        when(challenges.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(
                SpotlightChallenge.builder().id("know-yourself").kind("quiz").name("Know Yourself").tone("pink").minAge(13).status("live").build(),
                SpotlightChallenge.builder().id("clinic-shift").kind("game").name("Clinic Shift").tone("pink").cost(15).minAge(18).status("soon").build(),
                SpotlightChallenge.builder().id("pad-bank-run").kind("game").name("Pad Bank Run").tone("peach").cost(10).minAge(13).status("live").freePlays(3).build()));
        when(ranks.findByActiveTrueOrderByMinStarsAsc()).thenReturn(List.of(
                SpotlightRank.builder().id("curious").name("Curious").minStars(0).build(),
                SpotlightRank.builder().id("explorer").name("Explorer").minStars(15).build()));
        SpotlightGame quiz = game("know-yourself", new GameSummary(10, 4, 30, 5, 3, Set.of(today, today.minusDays(1)), 7));
        SpotlightGame bank = game("pad-bank-run", new GameSummary(6, 2, 0, 0, 0, Set.of(today.minusDays(2)), 1));

        SpotlightHomeResponse home = new SpotlightHomeService(users, clock, config, challenges, ranks, List.of(quiz, bank)).home(userId);

        assertThat(home.balance()).isEqualTo(240);
        assertThat(home.stats()).extracting("streak", "stars", "levelsDone", "rightAnswers", "bestRun", "topicsStarted")
                .containsExactly(3, 16, 6, 30L, 5, 3);
        assertThat(home.stats().week()).containsExactly(false, false, false, false, true, true, true);
        assertThat(home.stats().rank()).extracting("name", "nextName").containsExactly("Explorer", null);
        assertThat(home.challenges()).extracting("id", "cost", "locked", "freePlaysLeft").containsExactly(tuple("know-yourself", null, false, 0),
                tuple("clinic-shift", 15, true, 0), tuple("pad-bank-run", 10, false, 2));
        assertThat(home.challenges()).extracting("stars", "plays").containsExactly(tuple(10, 7L), tuple(0, 0L), tuple(6, 1L));
        assertThat(home.ranks()).extracting("name", "minStars").containsExactly(tuple("Curious", 0), tuple("Explorer", 15));
        assertThat(home.invite()).isEqualTo(new SpotlightInvite(true, 3, 3));
        assertThat(quiz.challengeId()).isEqualTo("know-yourself");
        assertThat(GameSummary.empty().stars()).isZero();
    }
}
