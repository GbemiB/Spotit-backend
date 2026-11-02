package com.spotit.api.spotlight.core.dto;

public record SpotlightInvite(
        boolean enabled,
        int everyDays,
        int maxDismissals
) {
}
