package com.spotit.api.spotlight.core.service;

import com.spotit.api.spotlight.core.dto.AdminPage;
import com.spotit.api.spotlight.core.dto.RankView;
import com.spotit.api.spotlight.core.entity.SpotlightRank;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public final class SpotlightRules {
    public static final int ADULT_AGE = 18;
    public static final int MAX_PAGE_SIZE = 200;
    static final int WEEK_DAYS = 7;

    private SpotlightRules() {
    }

    public static Integer ageOn(LocalDate dob, LocalDate today) {
        if (dob == null || today == null) {
            return null;
        }
        return Period.between(dob, today).getYears();
    }

    public static boolean isLocked(int minAge, Integer age) {
        if (minAge < ADULT_AGE) {
            return false;
        }
        return age == null || age < minAge;
    }

    public static String encodeCursor(int offset) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(offset).getBytes(StandardCharsets.UTF_8));
    }

    public static int decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return 0;
        }
        try {
            return Math.max(0, Integer.parseInt(new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8)));
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    public static int pageSize(Integer requested, int fallback) {
        int size = requested == null ? fallback : requested;
        return Math.max(1, Math.min(MAX_PAGE_SIZE, size));
    }

    public static <T> AdminPage<T> page(List<T> all, Integer page, Integer size, int fallback) {
        int n = pageSize(size, fallback);
        int p = page == null ? 0 : Math.max(0, page);
        long start = Math.min((long) p * n, all.size());
        long end = Math.min(start + n, all.size());
        return new AdminPage<>(all.subList((int) start, (int) end), p, n, all.size());
    }

    public static RankView rank(List<SpotlightRank> ranks, int stars) {
        if (ranks.isEmpty()) {
            return null;
        }
        SpotlightRank current = ranks.get(0);
        SpotlightRank next = null;
        for (SpotlightRank r : ranks) {
            if (r.getMinStars() <= stars) {
                current = r;
            } else {
                next = r;
                break;
            }
        }
        return new RankView(current.getId(), current.getName(), current.getMinStars(), next == null ? null : next.getName(),
                next == null ? null : next.getMinStars());
    }

    public static List<Boolean> week(Set<LocalDate> playDays, LocalDate today) {
        List<Boolean> days = new ArrayList<>();
        for (int back = WEEK_DAYS - 1; back >= 0; back--) {
            days.add(playDays.contains(today.minusDays(back)));
        }
        return days;
    }

    public static int streak(Set<LocalDate> playDays, LocalDate today) {
        LocalDate day = playDays.contains(today) ? today : today.minusDays(1);
        int count = 0;
        while (playDays.contains(day)) {
            count++;
            day = day.minusDays(1);
        }
        return count;
    }
}
