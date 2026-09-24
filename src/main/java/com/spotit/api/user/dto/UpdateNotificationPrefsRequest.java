package com.spotit.api.user.dto;

public record UpdateNotificationPrefsRequest(Boolean period, Boolean ovulation, Boolean dailyLog, Boolean digest) {
}
