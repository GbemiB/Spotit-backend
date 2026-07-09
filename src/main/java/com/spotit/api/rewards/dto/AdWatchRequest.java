package com.spotit.api.rewards.dto;

import jakarta.validation.constraints.NotBlank;

public record AdWatchRequest(@NotBlank String adNetwork, String adUnitId, String verificationToken) {
}
