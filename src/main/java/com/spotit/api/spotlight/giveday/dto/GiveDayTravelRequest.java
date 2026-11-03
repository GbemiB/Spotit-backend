package com.spotit.api.spotlight.giveday.dto;

import jakarta.validation.constraints.NotBlank;

public record GiveDayTravelRequest(@NotBlank String placeId, @NotBlank String mode) {
}
