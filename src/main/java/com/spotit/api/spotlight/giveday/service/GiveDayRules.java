package com.spotit.api.spotlight.giveday.service;

import com.spotit.api.spotlight.giveday.dto.GiveDayFact;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GiveDayRules {
    private static final String SEP = "|";
    private static final String SEP_RE = "\\|";

    private GiveDayRules() {
    }

    public static int stars(boolean absorbent, int good, int poor, int people, int total, int goodPicks) {
        if (!absorbent) {
            return 0;
        }
        return 1 + (poor == 0 && good >= goodPicks ? 1 : 0) + (people >= total ? 1 : 0);
    }

    public static int dayStars(Map<String, Integer> visits) {
        if (visits.isEmpty()) {
            return 0;
        }
        return visits.values().stream().mapToInt(Integer::intValue).sum() / visits.size();
    }

    public static List<Integer> decodeCounts(String raw) {
        List<Integer> counts = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return counts;
        }
        for (String part : raw.split(",")) {
            if (!part.isBlank()) {
                counts.add(Integer.parseInt(part.strip()));
            }
        }
        return counts;
    }

    public static String encodeCounts(List<Integer> counts) {
        List<String> parts = new ArrayList<>();
        (counts == null ? List.<Integer>of() : counts).forEach(n -> parts.add(String.valueOf(Math.max(1, n))));
        return String.join(",", parts);
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
