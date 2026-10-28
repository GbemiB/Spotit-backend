package com.spotit.api.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ApproveQuestionRequest(@NotBlank @Size(max = 120) String reviewedBy) {
}
