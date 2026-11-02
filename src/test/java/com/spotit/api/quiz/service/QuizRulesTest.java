package com.spotit.api.quiz.service;

import com.spotit.api.quiz.entity.QuizQuestion;
import com.spotit.api.quiz.entity.QuizRank;
import com.spotit.api.quiz.entity.QuizSeen;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class QuizRulesTest {
    private static QuizQuestion q(String id) {
        return QuizQuestion.builder().id(id).build();
    }

    private static QuizSeen seen(String id, long at) {
        return QuizSeen.builder().questionId(id).seenAt(Instant.ofEpochSecond(at)).build();
    }

    @Test
    void ageCountsWholeYears() {
        assertThat(QuizRules.ageOn(LocalDate.of(2010, 6, 15), LocalDate.of(2026, 6, 14))).isEqualTo(15);
        assertThat(QuizRules.ageOn(LocalDate.of(2010, 6, 15), LocalDate.of(2026, 6, 15))).isEqualTo(16);
        assertThat(QuizRules.ageOn(null, LocalDate.now())).isNull();
        assertThat(QuizRules.ageOn(LocalDate.now(), null)).isNull();
    }

    @Test
    void adultTopicsLockForTeensAndUnknownAges() {
        assertThat(QuizRules.isLocked(13, 13)).isFalse();
        assertThat(QuizRules.isLocked(18, 17)).isTrue();
        assertThat(QuizRules.isLocked(18, null)).isTrue();
        assertThat(QuizRules.isLocked(18, 18)).isFalse();
    }

    @Test
    void theCurrentLevelIsTheFirstNotYetCleared() {
        assertThat(QuizRules.currentLevel(Set.of(), 10)).isEqualTo(1);
        assertThat(QuizRules.currentLevel(Set.of(1, 2), 10)).isEqualTo(3);
        assertThat(QuizRules.currentLevel(Set.of(1, 2, 3), 3)).isEqualTo(3);
        assertThat(QuizRules.levelState(2, 3, true)).isEqualTo("cleared");
        assertThat(QuizRules.levelState(3, 3, false)).isEqualTo("current");
        assertThat(QuizRules.levelState(4, 3, false)).isEqualTo("locked");
    }

    @Test
    void theStreakCountsBackFromTodayOrYesterday() {
        LocalDate today = LocalDate.of(2026, 10, 28);
        assertThat(QuizRules.streak(Set.of(today, today.minusDays(1), today.minusDays(3)), today)).isEqualTo(2);
        assertThat(QuizRules.streak(Set.of(today.minusDays(1), today.minusDays(2)), today)).isEqualTo(2);
        assertThat(QuizRules.streak(Set.of(today.minusDays(2)), today)).isZero();
    }

    @Test
    void starsFollowTheHeartsLeft() {
        assertThat(QuizRules.starsFor(3, 3)).isEqualTo(3);
        assertThat(QuizRules.starsFor(2, 3)).isEqualTo(2);
        assertThat(QuizRules.starsFor(1, 3)).isEqualTo(1);
        assertThat(QuizRules.starsFor(0, 3)).isZero();
        assertThat(QuizRules.starsFor(1, 5)).isEqualTo(1);
    }

    @Test
    void unseenQuestionsComeFirstThenTheOldestSeen() {
        List<QuizQuestion> pool = List.of(q("a"), q("b"), q("c"), q("d"));
        List<QuizSeen> seen = List.of(seen("a", 30), seen("b", 10), seen("c", 20));

        List<QuizQuestion> picked = QuizRules.pickQuestions(pool, seen, 3, new Random(1));

        assertThat(picked).extracting(QuizQuestion::getId).containsExactly("d", "b", "c");
    }

    @Test
    void aSmallPoolGivesWhatItHas() {
        assertThat(QuizRules.pickQuestions(List.of(q("a")), List.of(), 5, new Random(1))).hasSize(1);
    }

    @Test
    void theRankFollowsStarsAndNamesTheNextOne() {
        List<QuizRank> ranks = List.of(QuizRank.builder().id("curious").name("Curious").minStars(0).build(),
                QuizRank.builder().id("explorer").name("Explorer").minStars(15).build(),
                QuizRank.builder().id("top").name("Know-It-All").minStars(90).build());

        assertThat(QuizRules.rank(ranks, 0)).extracting("id", "nextName", "nextMinStars").containsExactly("curious", "Explorer", 15);
        assertThat(QuizRules.rank(ranks, 33)).extracting("name", "minStars", "nextMinStars").containsExactly("Explorer", 15, 90);
        assertThat(QuizRules.rank(ranks, 150)).extracting("id", "nextName", "nextMinStars").containsExactly("top", null, null);
        assertThat(QuizRules.rank(List.of(), 10)).isNull();
    }

    @Test
    void theWeekRunsFromSixDaysAgoToToday() {
        LocalDate today = LocalDate.of(2026, 11, 2);
        assertThat(QuizRules.week(Set.of(today, today.minusDays(2), today.minusDays(9)), today))
                .containsExactly(false, false, false, false, true, false, true);
    }
}
