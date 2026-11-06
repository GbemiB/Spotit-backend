package com.spotit.api.spotlight.games.giveday.dto;

import jakarta.validation.constraints.NotBlank;

public record GiveDayHomeRequest(@NotBlank String areaId) {
}
