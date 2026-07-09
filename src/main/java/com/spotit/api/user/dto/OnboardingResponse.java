package com.spotit.api.user.dto;

public record OnboardingResponse(boolean onboarded, int cycleLength, int periodLength, String goal) {
}
