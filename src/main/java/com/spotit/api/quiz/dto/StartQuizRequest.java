package com.spotit.api.quiz.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record StartQuizRequest(@NotBlank String topicId, @Min(1) int level) {
}
