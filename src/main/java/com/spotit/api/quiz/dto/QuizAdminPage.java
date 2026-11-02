package com.spotit.api.quiz.dto;

import java.util.List;

public record QuizAdminPage<T>(List<T> items, int page, int size, long total) {
}
