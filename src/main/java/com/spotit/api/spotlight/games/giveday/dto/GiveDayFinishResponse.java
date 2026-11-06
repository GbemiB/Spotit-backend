package com.spotit.api.spotlight.games.giveday.dto;

import java.util.Map;

public record GiveDayFinishResponse(int stars, int helped, int spentToday, Map<String, Integer> places) {
}
