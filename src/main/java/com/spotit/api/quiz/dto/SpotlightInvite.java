package com.spotit.api.quiz.dto;

public record SpotlightInvite(
        boolean enabled,
        int everyDays,
        int maxDismissals
) {
}
