package com.spotit.api.spotlight.games.padbank.service;

import com.spotit.api.spotlight.games.padbank.dto.PadBankClue;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PadBankRulesTest {
    @Test
    void theRightPackAndRouteIsGreatAndAWrongPackIsOops() {
        assertThat(PadBankRules.result(true, true)).isEqualTo(PadBankRules.GREAT);
        assertThat(PadBankRules.result(true, false)).isEqualTo(PadBankRules.OK);
        assertThat(PadBankRules.result(false, true)).isEqualTo(PadBankRules.OOPS);
    }

    @Test
    void starsCountTheGoalsUnlessEveryHeartIsLost() {
        List<Boolean> all = PadBankRules.goals(3, 3, 3, 3, 3);
        assertThat(all).containsExactly(true, true, true);
        assertThat(PadBankRules.stars(all, 3)).isEqualTo(3);
        assertThat(PadBankRules.goals(3, 1, 3, 2, 3)).containsExactly(true, false, true);
        assertThat(PadBankRules.goals(2, 3, 3, 0, 3)).containsExactly(false, false, false);
        assertThat(PadBankRules.stars(all, 0)).isZero();
    }

    @Test
    void theFirstVisitorsAreFreeThenTheFeePackAndDeliveryAreCharged() {
        assertThat(PadBankRules.price(0, 2, 2, 5, true, 2)).isZero();
        assertThat(PadBankRules.price(1, 2, 2, 5, false, 2)).isZero();
        assertThat(PadBankRules.price(2, 2, 2, 5, false, 2)).isEqualTo(7);
        assertThat(PadBankRules.price(2, 2, 2, 5, true, 2)).isEqualTo(9);
        assertThat(PadBankRules.price(0, 0, 3, 6, false, 2)).isEqualTo(9);
    }

    @Test
    void cluesWhyTextsAndStockRoundTrip() {
        List<PadBankClue> clues = List.of(new PadBankClue("drop", "First period, today"), new PadBankClue("house", "A|B\\nline"));
        assertThat(PadBankRules.decodeClues(PadBankRules.encodeClues(clues)))
                .containsExactly(new PadBankClue("drop", "First period, today"), new PadBankClue("house", "A/B\\nline"));
        assertThat(PadBankRules.decodeClues(null)).isEmpty();
        assertThat(PadBankRules.encodeClues(null)).isEmpty();
        Map<String, String> why = new LinkedHashMap<>();
        why.put("starter", "Too small");
        why.put("comfort", null);
        assertThat(PadBankRules.decodeMap(PadBankRules.encodeMap(why))).containsEntry("starter", "Too small").containsEntry("comfort", "");
        assertThat(PadBankRules.decodeMap("solo")).containsEntry("solo", "");
        assertThat(PadBankRules.encodeMap(null)).isEmpty();
        Map<String, Integer> stock = new LinkedHashMap<>();
        stock.put("starter", 4);
        stock.put("school", 0);
        assertThat(PadBankRules.decodeStock(PadBankRules.encodeStock(stock))).isEqualTo(stock);
        assertThat(PadBankRules.decodeStock(" ")).isEmpty();
        assertThat(PadBankRules.decodeStock("bad,ok:2")).containsOnlyKeys("ok");
    }

    @Test
    void groupPacksRoundTripAndMatchInAnyOrder() {
        assertThat(PadBankRules.encodePacks(List.of(" sport ", "", "a,b", "comfort"))).isEqualTo("sport,ab,comfort");
        assertThat(PadBankRules.encodePacks(null)).isEmpty();
        assertThat(PadBankRules.decodePacks("sport, comfort,,")).containsExactly("sport", "comfort");
        assertThat(PadBankRules.decodePacks(" ")).isEmpty();
        assertThat(PadBankRules.decodePacks(null)).isEmpty();
        assertThat(PadBankRules.samePacks(List.of("sport", "sport", "comfort"), List.of("comfort", "sport", "sport"))).isTrue();
        assertThat(PadBankRules.samePacks(List.of("sport", "sport"), List.of("sport"))).isFalse();
        assertThat(PadBankRules.count(List.of("a", "b", "a"))).containsEntry("a", 2).containsEntry("b", 1);
    }

    @Test
    void theOopsTipExplainsTheGroupFirstThenTheWrongPack() {
        Map<String, String> why = Map.of("school", "Not a term.");
        assertThat(PadBankRules.oopsTip(List.of("a", "a"), List.of("a"), why, "One each.", "Look again.")).isEqualTo("One each.");
        assertThat(PadBankRules.oopsTip(List.of("a", "a"), List.of("a", "school"), why, "One each.", "Look again.")).isEqualTo("Not a term.");
        assertThat(PadBankRules.oopsTip(List.of("a", "a"), List.of("a", "b"), why, "One each.", "Look again.")).isEqualTo("One each.");
        assertThat(PadBankRules.oopsTip(List.of("a"), List.of("b"), why, null, "Look again.")).isEqualTo("Look again.");
        assertThat(PadBankRules.oopsTip(List.of("a"), List.of("b"), Map.of("b", " "), " ", "Look again.")).isEqualTo("Look again.");
    }
}
