package com.spotit.api.user.dto;

import java.util.List;

/**
 * The four switches plus the settings in effect for this user: her own choice where she has made one, otherwise
 * the server default. Days-before lists count down to the day itself (0); maxDaysBefore is the largest the app may offer.
 */
public record NotificationPrefsResponse(boolean period, boolean ovulation, boolean dailyLog, boolean digest,
                                        String morningTime, String eveningTime,
                                        List<Integer> periodDaysBefore, List<Integer> ovulationDaysBefore, int maxDaysBefore) {
}
