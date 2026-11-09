package com.spotit.api.spotlight.games.giveday.dto;

public record GiveDayLevelView(int number, String name, int radiusKm, int starsNeeded, int passCost, int giftBudget, int giftMax, boolean extrasFree) {
}
