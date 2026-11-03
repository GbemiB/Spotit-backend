package com.spotit.api.spotlight.giveday.dto;

import jakarta.validation.constraints.NotBlank;

public record GiveDayHomeRequest(@NotBlank String areaId) {
}
