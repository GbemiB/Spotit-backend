package com.spotit.api.spotlight.padbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PadBankServeRequest(@NotNull Integer round, @NotBlank String pack, @NotBlank String route) {
}
