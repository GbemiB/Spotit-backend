package com.spotit.api.spotlight.games.giveday.dto;

import jakarta.validation.constraints.NotBlank;

public record GiveDayTravelRequest(@NotBlank String placeId, @NotBlank String mode) {
}
