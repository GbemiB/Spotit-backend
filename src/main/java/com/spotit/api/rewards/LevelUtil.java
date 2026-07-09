package com.spotit.api.rewards;

import java.util.List;

/** Mirrors spotit-mobile's src/shared/utils/levels.js thresholds exactly. */
public final class LevelUtil {

    private LevelUtil() {
    }

    public record LevelDef(String name, long lo, long hi) {
    }

    private static final List<LevelDef> LEVELS = List.of(
            new LevelDef("Blush", 0, 500),
            new LevelDef("Petal", 500, 2000),
            new LevelDef("Rosé", 2000, 5000),
            new LevelDef("Bloom", 5000, 10000),
            new LevelDef("Wildflower", 10000, 25000),
            new LevelDef("Moonflower", 25000, 50000)
    );

    public static final String MAX_LEVEL_NAME = "Goddess";

    public static final List<String> LEVEL_ORDER = List.of(
            "Blush", "Petal", "Rosé", "Bloom", "Wildflower", "Moonflower", MAX_LEVEL_NAME
    );

    public record LevelInfo(String name, long lo, long hi, String nextLevelName, Long pointsToNextLevel, double pct) {
    }

    public static LevelInfo levelFor(long points) {
        for (int i = 0; i < LEVELS.size(); i++) {
            LevelDef def = LEVELS.get(i);
            if (points < def.hi()) {
                String next = i + 1 < LEVELS.size() ? LEVELS.get(i + 1).name() : MAX_LEVEL_NAME;
                double pct = (points - def.lo()) / (double) (def.hi() - def.lo());
                return new LevelInfo(def.name(), def.lo(), def.hi(), next, def.hi() - points, pct);
            }
        }
        return new LevelInfo(MAX_LEVEL_NAME, 50_000, 50_000, null, null, 1.0);
    }

    /** True if `levelName` meets or exceeds the `minLevel` requirement, using LEVEL_ORDER rank. */
    public static boolean meetsMinLevel(String levelName, String minLevel) {
        return LEVEL_ORDER.indexOf(levelName) >= LEVEL_ORDER.indexOf(minLevel);
    }
}
