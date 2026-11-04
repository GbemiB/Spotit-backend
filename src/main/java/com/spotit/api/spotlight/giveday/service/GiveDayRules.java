package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.spotlight.giveday.dto.GiveDayFact;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.ToIntFunction;

public final class GiveDayRules {
    private static final String SEP = "|";
    private static final String SEP_RE = "\\|";
    private static final int MINUTE_STEP = 5;
    public static final int MAX_STARS = 3;

    private GiveDayRules() {
    }

    public static int stars(boolean absorbent, int good, int poor, int goodPicks) {
        if (!absorbent) {
            return 0;
        }
        return 1 + (poor == 0 ? 1 : 0) + (good >= goodPicks ? 1 : 0);
    }

    public static int giftPaid(int giftCost, int giftFree, boolean extrasFree) {
        return extrasFree ? 0 : Math.max(0, giftCost - giftFree);
    }

    public static int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static int minutes(int distance, int unitsPerMinute) {
        int raw = (int) Math.round((double) distance / Math.max(1, unitsPerMinute) / MINUTE_STEP) * MINUTE_STEP;
        return Math.max(MINUTE_STEP, raw);
    }

    public static <T> T ring(int x1, int y1, int x2, int y2, int unitsPerKm, List<T> levels, ToIntFunction<T> radiusKm) {
        double km = Math.hypot(x1 - x2, y1 - y2) / Math.max(1, unitsPerKm);
        for (T l : levels) {
            if (km <= radiusKm.applyAsInt(l)) {
                return l;
            }
        }
        return levels.get(levels.size() - 1);
    }

    public static <T> int level(int stars, List<T> levels, ToIntFunction<T> need, ToIntFunction<T> number) {
        int best = levels.isEmpty() ? 1 : number.applyAsInt(levels.get(0));
        for (T l : levels) {
            if (stars >= need.applyAsInt(l)) {
                best = Math.max(best, number.applyAsInt(l));
            }
        }
        return best;
    }

    public static int bestStars(List<Map<String, Integer>> days) {
        Map<String, Integer> best = new LinkedHashMap<>();
        days.forEach(d -> d.forEach((place, stars) -> best.merge(place, stars, Math::max)));
        return best.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static Set<String> decodeIds(String raw) {
        Set<String> ids = new LinkedHashSet<>();
        if (raw == null || raw.isBlank()) {
            return ids;
        }
        for (String part : raw.split(",")) {
            if (!part.isBlank()) {
                ids.add(part.strip());
            }
        }
        return ids;
    }

    public static Set<Integer> decodePasses(String raw) {
        Set<Integer> passes = new TreeSet<>();
        if (raw == null || raw.isBlank()) {
            return passes;
        }
        for (String part : raw.split(",")) {
            if (!part.isBlank()) {
                passes.add(Integer.parseInt(part.strip()));
            }
        }
        return passes;
    }

    public static String encodePasses(Set<Integer> passes) {
        List<String> parts = new ArrayList<>();
        passes.forEach(n -> parts.add(String.valueOf(n)));
        return String.join(",", parts);
    }

    public static int dayStars(Map<String, Integer> visits) {
        if (visits.isEmpty()) {
            return 0;
        }
        return visits.values().stream().mapToInt(Integer::intValue).sum() / visits.size();
    }

    public static List<GiveDayFact> decodeFacts(String raw) {
        List<GiveDayFact> facts = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return facts;
        }
        for (String line : raw.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            String[] p = line.split(SEP_RE, 3);
            facts.add(new GiveDayFact(p[0], p.length > 1 ? p[1] : "", p.length > 2 ? p[2] : ""));
        }
        return facts;
    }

    public static String encodeFacts(List<GiveDayFact> facts) {
        List<String> lines = new ArrayList<>();
        for (GiveDayFact f : facts == null ? List.<GiveDayFact>of() : facts) {
            lines.add(clean(f.icon()) + SEP + clean(f.tone()) + SEP + clean(f.text()));
        }
        return String.join("\n", lines);
    }

    public static Map<String, Integer> decodeVisits(String raw) {
        Map<String, Integer> visits = new LinkedHashMap<>();
        if (raw == null || raw.isBlank()) {
            return visits;
        }
        for (String part : raw.split(",")) {
            String[] kv = part.split(":");
            if (kv.length == 2) {
                visits.put(kv[0], Integer.parseInt(kv[1]));
            }
        }
        return visits;
    }

    public static String encodeVisits(Map<String, Integer> visits) {
        List<String> parts = new ArrayList<>();
        visits.forEach((k, v) -> parts.add(k + ":" + v));
        return String.join(",", parts);
    }

    private static String clean(String s) {
        return s == null ? "" : s.replace("\n", " ").replace(SEP, "/").strip();
    }
}
