package com.spotit.api.spotlight.giveday.dto;

public record GiveDayAreaView(String id, String name, String region, int x, int y, int places, int level, boolean locked, int visited) {
}
