package com.spotit.api.spotlight.giveday.dto;

import java.util.List;

public record GiveDayHomeResponse(String home, int x, int y, List<GiveDayAreaView> areas) {
}
