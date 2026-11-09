package com.spotit.api.spotlight.games.quiz.dto;

import jakarta.validation.constraints.Min;

public record HintRequest(@Min(0) int round) {
}
