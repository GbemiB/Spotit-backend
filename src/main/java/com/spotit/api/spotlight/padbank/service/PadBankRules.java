package com.spotit.api.spotlight.padbank.service;

import com.spotit.api.spotlight.padbank.dto.PadBankClue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PadBankRules {
    public static final String GREAT = "great";
    public static final String OK = "ok";
    public static final String OOPS = "oops";
    private static final String SEP = "|";
    private static final String SEP_RE = "\\|";

    private PadBankRules() {
    }

    public static String result(boolean packOk, boolean routeOk) {
        if (packOk && routeOk) {
            return GREAT;
        }
        return packOk ? OK : OOPS;
    }

    public static List<Boolean> goals(int served, int rightPack, int rightWay, int hearts, int total) {
        return List.of(served >= total, hearts > 0 && rightPack >= total, hearts > 0 && rightWay >= total);
    }

    public static int price(int round, int freeVisitors, int visitorFee, int packCost, boolean delivery, int deliveryCost) {
        if (round < freeVisitors) {
            return 0;
        }
        return visitorFee + packCost + (delivery ? deliveryCost : 0);
    }

    public static int stars(List<Boolean> goals, int hearts) {
        if (hearts <= 0) {
            return 0;
        }
        return (int) goals.stream().filter(Boolean::booleanValue).count();
    }

    public static String encodeClues(List<PadBankClue> clues) {
        List<String> lines = new ArrayList<>();
        for (PadBankClue c : clues == null ? List.<PadBankClue>of() : clues) {
            lines.add(clean(c.icon()) + SEP + clean(c.text()));
        }
        return String.join("\n", lines);
    }

    public static List<PadBankClue> decodeClues(String raw) {
        List<PadBankClue> clues = new ArrayList<>();
        for (String[] parts : split(raw)) {
            clues.add(new PadBankClue(parts[0], parts.length > 1 ? parts[1] : ""));
        }
        return clues;
    }

    public static String encodeMap(Map<String, String> map) {
        List<String> lines = new ArrayList<>();
        (map == null ? Map.<String, String>of() : map).forEach((k, v) -> lines.add(clean(k) + SEP + clean(v)));
        return String.join("\n", lines);
    }

    public static Map<String, String> decodeMap(String raw) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String[] parts : split(raw)) {
            map.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
        return map;
    }

    public static String encodeLines(List<String> lines) {
        List<String> clean = new ArrayList<>();
        for (String line : lines == null ? List.<String>of() : lines) {
            String c = clean(line);
            if (!c.isEmpty()) {
                clean.add(c);
            }
        }
        return String.join("\n", clean);
    }

    public static List<String> decodeLines(String raw) {
        List<String> lines = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return lines;
        }
        for (String line : raw.split("\n")) {
            if (!line.isBlank()) {
                lines.add(line.strip());
            }
        }
        return lines;
    }

    public static String encodeStock(Map<String, Integer> stock) {
        List<String> parts = new ArrayList<>();
        stock.forEach((k, v) -> parts.add(k + ":" + v));
        return String.join(",", parts);
    }

    public static Map<String, Integer> decodeStock(String raw) {
        Map<String, Integer> stock = new LinkedHashMap<>();
        if (raw == null || raw.isBlank()) {
            return stock;
        }
        for (String part : raw.split(",")) {
            String[] kv = part.split(":");
            if (kv.length == 2) {
                stock.put(kv[0], Integer.parseInt(kv[1]));
            }
        }
        return stock;
    }

    private static List<String[]> split(String raw) {
        List<String[]> rows = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return rows;
        }
        for (String line : raw.split("\n")) {
            if (!line.isBlank()) {
                rows.add(line.split(SEP_RE, 2));
            }
        }
        return rows;
    }

    private static String clean(String s) {
        return s == null ? "" : s.replace("\n", " ").replace(SEP, "/").strip();
    }
}
