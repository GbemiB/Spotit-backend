package com.spotit.api.user.dto;

public record NotificationPrefsResponse(boolean period, boolean ovulation, boolean dailyLog, boolean digest) {
}
