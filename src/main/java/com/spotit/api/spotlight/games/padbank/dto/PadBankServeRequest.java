package com.spotit.api.spotlight.games.padbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PadBankServeRequest(@NotNull Integer round, String pack, List<String> packs, @NotBlank String route) {
    public List<String> picked() {
        if (packs != null && !packs.isEmpty()) {
            return packs.stream().map(p -> p == null ? "" : p.strip()).toList();
        }
        return pack == null || pack.isBlank() ? List.of() : List.of(pack.strip());
    }
}
