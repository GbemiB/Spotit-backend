package com.spotit.api.spotlight.giveday.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record GiveDayGiveRequest(@NotBlank String placeId, @NotEmpty List<String> products) {
}
