package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.spotlight.padbank.dto.PadBankClue;
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
        List<Boolean> all = PadBankRules.goals(3, 3, 45, 3, 3, 40, true);
        assertThat(all).containsExactly(true, true, true);
        assertThat(PadBankRules.stars(all, 3)).isEqualTo(3);
        assertThat(PadBankRules.goals(3, 1, 30, 2, 3, 40, true)).containsExactly(true, false, false);
        assertThat(PadBankRules.goals(3, 3, 80, 3, 3, 40, false).get(2)).isFalse();
        assertThat(PadBankRules.stars(all, 0)).isZero();
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
}
