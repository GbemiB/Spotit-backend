package com.spotit.api.spotlight.quiz.service;

import com.spotit.api.spotlight.core.service.SpotlightRules;
import com.spotit.api.spotlight.quiz.entity.QuizLevel;
import com.spotit.api.spotlight.quiz.entity.QuizQuestion;
import com.spotit.api.spotlight.core.entity.SpotlightRank;
import com.spotit.api.spotlight.quiz.entity.QuizSeen;
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
        assertThat(SpotlightRules.ageOn(LocalDate.of(2010, 6, 15), LocalDate.of(2026, 6, 14))).isEqualTo(15);
        assertThat(SpotlightRules.ageOn(LocalDate.of(2010, 6, 15), LocalDate.of(2026, 6, 15))).isEqualTo(16);
        assertThat(SpotlightRules.ageOn(null, LocalDate.now())).isNull();
        assertThat(SpotlightRules.ageOn(LocalDate.now(), null)).isNull();
    }

    @Test
    void adultTopicsLockForTeensAndUnknownAges() {
        assertThat(SpotlightRules.isLocked(13, 13)).isFalse();
        assertThat(SpotlightRules.isLocked(18, 17)).isTrue();
        assertThat(SpotlightRules.isLocked(18, null)).isTrue();
        assertThat(SpotlightRules.isLocked(18, 18)).isFalse();
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
        assertThat(SpotlightRules.streak(Set.of(today, today.minusDays(1), today.minusDays(3)), today)).isEqualTo(2);
        assertThat(SpotlightRules.streak(Set.of(today.minusDays(1), today.minusDays(2)), today)).isEqualTo(2);
        assertThat(SpotlightRules.streak(Set.of(today.minusDays(2)), today)).isZero();
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
    void freeLevelsCountsTheFreeLevelsEveryTopicStartsWith() {
        QuizLevel free1 = QuizLevel.builder().number(1).cost(0).build();
        QuizLevel free2 = QuizLevel.builder().number(2).cost(0).build();
        QuizLevel paid3 = QuizLevel.builder().number(3).cost(10).build();
        QuizLevel free3 = QuizLevel.builder().number(3).cost(0).build();

        assertThat(QuizRules.freeLevels(List.of(List.of(paid3, free2, free1), List.of(free1, free2, free3)))).isEqualTo(2);
        assertThat(QuizRules.freeLevels(List.of(List.of(free1, free2, free3)))).isEqualTo(3);
        assertThat(QuizRules.freeLevels(List.of())).isZero();
    }

    @Test
    void theRankFollowsStarsAndNamesTheNextOne() {
        List<SpotlightRank> ranks = List.of(SpotlightRank.builder().id("curious").name("Curious").minStars(0).build(),
                SpotlightRank.builder().id("explorer").name("Explorer").minStars(15).build(),
                SpotlightRank.builder().id("top").name("Know-It-All").minStars(90).build());

        assertThat(SpotlightRules.rank(ranks, 0)).extracting("id", "nextName", "nextMinStars").containsExactly("curious", "Explorer", 15);
        assertThat(SpotlightRules.rank(ranks, 33)).extracting("name", "minStars", "nextMinStars").containsExactly("Explorer", 15, 90);
        assertThat(SpotlightRules.rank(ranks, 150)).extracting("id", "nextName", "nextMinStars").containsExactly("top", null, null);
        assertThat(SpotlightRules.rank(List.of(), 10)).isNull();
    }

    @Test
    void theWeekRunsFromSixDaysAgoToToday() {
        LocalDate today = LocalDate.of(2026, 11, 2);
        assertThat(SpotlightRules.week(Set.of(today, today.minusDays(2), today.minusDays(9)), today))
                .containsExactly(false, false, false, false, true, false, true);
    }

    @Test
    void cursorsRoundTripAndBadOnesStartAtTheTop() {
        assertThat(SpotlightRules.decodeCursor(SpotlightRules.encodeCursor(16))).isEqualTo(16);
        assertThat(SpotlightRules.decodeCursor(null)).isZero();
        assertThat(SpotlightRules.decodeCursor("not a cursor!")).isZero();
        assertThat(SpotlightRules.decodeCursor(SpotlightRules.encodeCursor(-4))).isZero();
    }

    @Test
    void pagesAreClampedToSensibleSizes() {
        List<Integer> all = List.of(1, 2, 3, 4, 5);
        assertThat(SpotlightRules.page(all, 1, 2, 50).items()).containsExactly(3, 4);
        assertThat(SpotlightRules.page(all, 9, 2, 50).items()).isEmpty();
        assertThat(SpotlightRules.page(all, -1, 0, 50)).extracting("page", "size", "total").containsExactly(0, 1, 5L);
        assertThat(SpotlightRules.page(all, null, null, 3).items()).containsExactly(1, 2, 3);
        assertThat(SpotlightRules.pageSize(1000, 8)).isEqualTo(SpotlightRules.MAX_PAGE_SIZE);
    }
}
