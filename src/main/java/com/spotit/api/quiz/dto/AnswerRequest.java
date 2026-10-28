package com.spotit.api.quiz.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record AnswerRequest(@Min(0) int round, @Pattern(regexp = "myth|fact") String choice) {
}
