package com.spotit.api.user.dto;

import java.util.List;

/**
 * The four switches plus the settings in effect for this user. morningTime and eveningTime are the server's reminder
 * times, shown to her but not hers to change. The period countdown is her own choice where she has made one, otherwise
 * the server default; it counts down to the day itself (0), and maxDaysBefore is the largest the app may offer.
 * Ovulation has no countdown: it is announced on the day.
 */
public record NotificationPrefsResponse(boolean period, boolean ovulation, boolean dailyLog, boolean digest,
                                        String morningTime, String eveningTime,
                                        List<Integer> periodDaysBefore, int maxDaysBefore) {
}
