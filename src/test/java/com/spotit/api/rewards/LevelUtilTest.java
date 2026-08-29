package com.spotit.api.rewards;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LevelUtilTest {

    // Same thresholds the original hardcoded LEVELS array had — now the shape callers pass in
    // (see LevelDefinitionService), LevelUtil itself no longer holds any tier data.
    private static final List<LevelUtil.LevelDef> LEVELS = List.of(
            new LevelUtil.LevelDef("Blush", 0, 500),
            new LevelUtil.LevelDef("Petal", 500, 2000),
            new LevelUtil.LevelDef("Rosé", 2000, 5000),
            new LevelUtil.LevelDef("Bloom", 5000, 10000),
            new LevelUtil.LevelDef("Wildflower", 10000, 25000),
            new LevelUtil.LevelDef("Moonflower", 25000, 50000)
    );
    private static final List<String> LEVEL_ORDER =
            List.of("Blush", "Petal", "Rosé", "Bloom", "Wildflower", "Moonflower", LevelUtil.MAX_LEVEL_NAME);

    @Test
    void zeroPointsIsBlushAtStart() {
        LevelUtil.LevelInfo info = LevelUtil.levelFor(0, LEVELS);

        assertThat(info.name()).isEqualTo("Blush");
        assertThat(info.lo()).isEqualTo(0);
        assertThat(info.hi()).isEqualTo(500);
        assertThat(info.nextLevelName()).isEqualTo("Petal");
        assertThat(info.pointsToNextLevel()).isEqualTo(500);
        assertThat(info.pct()).isEqualTo(0.0);
    }

    @Test
    void pointsJustBelowThresholdStayInLowerLevel() {
        LevelUtil.LevelInfo info = LevelUtil.levelFor(499, LEVELS);

        assertThat(info.name()).isEqualTo("Blush");
        assertThat(info.pointsToNextLevel()).isEqualTo(1);
    }

    @Test
    void pointsAtThresholdRollIntoNextLevel() {
        LevelUtil.LevelInfo info = LevelUtil.levelFor(500, LEVELS);

        assertThat(info.name()).isEqualTo("Petal");
        assertThat(info.lo()).isEqualTo(500);
        assertThat(info.hi()).isEqualTo(2000);
        assertThat(info.pct()).isEqualTo(0.0);
    }

    @Test
    void midLevelComputesProgressPercentage() {
        // Rosé spans [2000, 5000); 3500 is exactly halfway through the 3000-point range.
        LevelUtil.LevelInfo info = LevelUtil.levelFor(3500, LEVELS);

        assertThat(info.name()).isEqualTo("Rosé");
        assertThat(info.pct()).isEqualTo(0.5);
        assertThat(info.pointsToNextLevel()).isEqualTo(1500);
    }

    @Test
    void topOfDefinedLevelsIsMoonflowerWithGoddessAsNext() {
        LevelUtil.LevelInfo info = LevelUtil.levelFor(49_999, LEVELS);

        assertThat(info.name()).isEqualTo("Moonflower");
        assertThat(info.nextLevelName()).isEqualTo(LevelUtil.MAX_LEVEL_NAME);
    }

    @ParameterizedTest
    @CsvSource({"50000", "50001", "1000000"})
    void pointsAtOrBeyondTheTopFallThroughToGoddess(long points) {
        LevelUtil.LevelInfo info = LevelUtil.levelFor(points, LEVELS);

        assertThat(info.name()).isEqualTo("Goddess");
        assertThat(info.lo()).isEqualTo(50_000);
        assertThat(info.hi()).isEqualTo(50_000);
        assertThat(info.nextLevelName()).isNull();
        assertThat(info.pointsToNextLevel()).isNull();
        assertThat(info.pct()).isEqualTo(1.0);
    }

    @Test
    void meetsMinLevelUsesRankNotPoints() {
        assertThat(LevelUtil.meetsMinLevel("Blush", "Petal", LEVEL_ORDER)).isFalse();
        assertThat(LevelUtil.meetsMinLevel("Petal", "Petal", LEVEL_ORDER)).isTrue();
        assertThat(LevelUtil.meetsMinLevel("Rosé", "Petal", LEVEL_ORDER)).isTrue();
        assertThat(LevelUtil.meetsMinLevel("Goddess", "Blush", LEVEL_ORDER)).isTrue();
    }
}
