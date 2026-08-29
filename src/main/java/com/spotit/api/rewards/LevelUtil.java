package com.spotit.api.rewards;

import java.util.List;

public final class LevelUtil {
    private LevelUtil() {
    }

    public record LevelDef(String name, long lo, long hi) {
    }

    public static final String MAX_LEVEL_NAME = "Goddess";

    public record LevelInfo(String name, long lo, long hi, String nextLevelName, Long pointsToNextLevel, double pct) {
    }

    public static LevelInfo levelFor(long points, List<LevelDef> levels) {
        for (int i = 0; i < levels.size(); i++) {
            LevelDef def = levels.get(i);
            if (points < def.hi()) {
                String next = i + 1 < levels.size() ? levels.get(i + 1).name() : MAX_LEVEL_NAME;
                double pct = (points - def.lo()) / (double) (def.hi() - def.lo());
                return new LevelInfo(def.name(), def.lo(), def.hi(), next, def.hi() - points, pct);
            }
        }
        long top = levels.isEmpty() ? 0 : levels.get(levels.size() - 1).hi();
        return new LevelInfo(MAX_LEVEL_NAME, top, top, null, null, 1.0);
    }

    public static boolean meetsMinLevel(String levelName, String minLevel, List<String> levelOrder) {
        return levelOrder.indexOf(levelName) >= levelOrder.indexOf(minLevel);
    }
}
