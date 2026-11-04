package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.spotlight.giveday.dto.GiveDayFact;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GiveDayRulesTest {
    @Test
    void starsNeedSomethingAbsorbentThenRewardGoodPicksAndHelpingEveryone() {
        assertThat(GiveDayRules.stars(false, 5, 0, 3)).isZero();
        assertThat(GiveDayRules.stars(true, 1, 1, 3)).isEqualTo(1);
        assertThat(GiveDayRules.stars(true, 1, 0, 3)).isEqualTo(2);
        assertThat(GiveDayRules.stars(true, 3, 1, 3)).isEqualTo(2);
        assertThat(GiveDayRules.stars(true, 3, 0, 3)).isEqualTo(3);
    }

    @Test
    void theDayIsTheAverageOfThePlacesHelped() {
        assertThat(GiveDayRules.dayStars(Map.of())).isZero();
        Map<String, Integer> visits = new LinkedHashMap<>();
        visits.put("a", 3);
        visits.put("b", 2);
        assertThat(GiveDayRules.dayStars(visits)).isEqualTo(2);
        assertThat(GiveDayRules.decodeVisits(GiveDayRules.encodeVisits(visits))).isEqualTo(visits);
        assertThat(GiveDayRules.decodeVisits(" ")).isEmpty();
        assertThat(GiveDayRules.decodeVisits("bad,ok:1")).containsOnlyKeys("ok");
    }

    @Test
    void travelTimeLevelsAndPassesFollowTheMap() {
        assertThat(GiveDayRules.distance(0, 0, 30, -40)).isEqualTo(70);
        assertThat(GiveDayRules.minutes(200, 8)).isEqualTo(25);
        assertThat(GiveDayRules.minutes(10, 50)).isEqualTo(5);
        assertThat(GiveDayRules.minutes(1000, 0)).isEqualTo(1000);
        List<int[]> rings = List.of(new int[]{1, 3, 0}, new int[]{2, 8, 3}, new int[]{3, 15, 10});
        assertThat(GiveDayRules.ring(0, 0, 300, 0, 100, rings, r -> r[1])[0]).isEqualTo(1);
        assertThat(GiveDayRules.ring(0, 0, 600, 600, 100, rings, r -> r[1])[0]).isEqualTo(3);
        assertThat(GiveDayRules.ring(0, 0, 9000, 0, 100, rings, r -> r[1])[0]).isEqualTo(3);
        assertThat(GiveDayRules.level(5, rings, r -> r[2], r -> r[0])).isEqualTo(2);
        assertThat(GiveDayRules.level(5, List.<int[]>of(), r -> r[2], r -> r[0])).isEqualTo(1);
        assertThat(GiveDayRules.bestStars(List.of(Map.of("a", 1, "b", 3), Map.of("a", 2)))).isEqualTo(5);
        assertThat(GiveDayRules.decodePasses(GiveDayRules.encodePasses(new java.util.TreeSet<>(java.util.Set.of(4, 3))))).containsExactly(3, 4);
        assertThat(GiveDayRules.decodePasses(null)).isEmpty();
        assertThat(GiveDayRules.decodePasses("3, ,")).containsExactly(3);
    }

    @Test
    void factsRoundTrip() {
        List<GiveDayFact> facts = List.of(new GiveDayFact("girls", "pink", "18 girls"), new GiveDayFact("drop", null, "Water | soap"));
        assertThat(GiveDayRules.decodeFacts(GiveDayRules.encodeFacts(facts)))
                .containsExactly(new GiveDayFact("girls", "pink", "18 girls"), new GiveDayFact("drop", "", "Water / soap"));
        assertThat(GiveDayRules.decodeFacts("\nsolo")).containsExactly(new GiveDayFact("solo", "", ""));
        assertThat(GiveDayRules.decodeFacts(null)).isEmpty();
        assertThat(GiveDayRules.encodeFacts(null)).isEmpty();
    }

    @Test
    void onlyGiftsAboveTheFreeAmountArePaidAndNoneWhenExtrasAreFree() {
        assertThat(GiveDayRules.giftPaid(4, 5, false)).isZero();
        assertThat(GiveDayRules.giftPaid(9, 5, false)).isEqualTo(4);
        assertThat(GiveDayRules.giftPaid(9, 5, true)).isZero();
    }
}
