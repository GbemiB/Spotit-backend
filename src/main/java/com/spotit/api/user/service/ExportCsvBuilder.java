package com.spotit.api.user.service;

import com.spotit.api.user.dto.ExportDataResponse;
import com.spotit.api.user.dto.UserResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Builds a spreadsheet-friendly CSV (opens directly in Excel/Sheets/Numbers) out of a user's
 * export data, laid out as three clearly labelled sections rather than a raw data dump.
 */
public final class ExportCsvBuilder {

    private ExportCsvBuilder() {
    }

    public static byte[] build(ExportDataResponse data) {
        StringBuilder sb = new StringBuilder();
        sb.append("﻿"); // UTF-8 BOM so Excel picks the right encoding

        appendProfile(sb, data.profile());
        sb.append('\n');
        appendLogs(sb, data.logs());
        sb.append('\n');
        appendPointsHistory(sb, data.pointsHistory());

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void appendProfile(StringBuilder sb, UserResponse profile) {
        row(sb, "PROFILE");
        row(sb, "Name", profile.firstName() + " " + profile.lastName());
        row(sb, "Email", profile.email());
        row(sb, "Date of birth", str(profile.dob()));
        row(sb, "Goal", str(profile.goal()));
        row(sb, "Cycle length (days)", String.valueOf(profile.cycleLength()));
        row(sb, "Period length (days)", String.valueOf(profile.periodLength()));
        row(sb, "Last period date", str(profile.lastPeriodDate()));
        row(sb, "Premium", profile.isPremium() ? "Yes" : "No");
    }

    private static void appendLogs(StringBuilder sb, List<Map<String, Object>> logs) {
        row(sb, "CYCLE LOGS");
        row(sb, "Date", "Flow", "Mood", "Symptoms", "Notes");
        for (Map<String, Object> log : logs) {
            row(sb, str(log.get("date")), str(log.get("flow")), str(log.get("mood")), str(log.get("symptoms")), str(log.get("notes")));
        }
    }

    private static void appendPointsHistory(StringBuilder sb, List<Map<String, Object>> history) {
        row(sb, "POINTS HISTORY");
        row(sb, "Date", "Activity", "Points");
        for (Map<String, Object> entry : history) {
            row(sb, str(entry.get("date")), str(entry.get("label")), str(entry.get("delta")));
        }
    }

    private static String str(Object value) {
        return value == null ? "" : value.toString();
    }

    private static void row(StringBuilder sb, String... cells) {
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escape(cells[i]));
        }
        sb.append('\n');
    }

    private static String escape(String cell) {
        if (cell == null) return "";
        if (cell.contains(",") || cell.contains("\"") || cell.contains("\n")) {
            return "\"" + cell.replace("\"", "\"\"") + "\"";
        }
        return cell;
    }
}
