package com.spotit.api.spotlight.giveday.dto;

public record GiveDayTravelResponse(String placeId, String mode, int minutes, int clock, int x, int y, int spent, int spentToday, long balance) {
}
