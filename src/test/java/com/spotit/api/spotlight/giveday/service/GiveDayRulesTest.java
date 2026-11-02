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
        assertThat(GiveDayRules.stars(false, 5, 0, 18, 18, 3)).isZero();
        assertThat(GiveDayRules.stars(true, 1, 0, 6, 18, 3)).isEqualTo(1);
        assertThat(GiveDayRules.stars(true, 3, 0, 6, 18, 3)).isEqualTo(2);
        assertThat(GiveDayRules.stars(true, 3, 1, 18, 18, 3)).isEqualTo(2);
        assertThat(GiveDayRules.stars(true, 4, 0, 18, 18, 3)).isEqualTo(3);
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
    void groupSizesAndFactsRoundTrip() {
        assertThat(GiveDayRules.decodeCounts(GiveDayRules.encodeCounts(List.of(6, 12, 0)))).containsExactly(6, 12, 1);
        assertThat(GiveDayRules.decodeCounts(null)).isEmpty();
        assertThat(GiveDayRules.decodeCounts("4, ,8")).containsExactly(4, 8);
        assertThat(GiveDayRules.encodeCounts(null)).isEmpty();
        List<GiveDayFact> facts = List.of(new GiveDayFact("girls", "pink", "18 girls"), new GiveDayFact("drop", null, "Water | soap"));
        assertThat(GiveDayRules.decodeFacts(GiveDayRules.encodeFacts(facts)))
                .containsExactly(new GiveDayFact("girls", "pink", "18 girls"), new GiveDayFact("drop", "", "Water / soap"));
        assertThat(GiveDayRules.decodeFacts("\nsolo")).containsExactly(new GiveDayFact("solo", "", ""));
        assertThat(GiveDayRules.decodeFacts(null)).isEmpty();
        assertThat(GiveDayRules.encodeFacts(null)).isEmpty();
    }
}
