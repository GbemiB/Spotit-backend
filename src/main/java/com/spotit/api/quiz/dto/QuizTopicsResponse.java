package com.spotit.api.quiz.dto;

import java.util.List;

public record QuizTopicsResponse(boolean underAge, int topicCount, List<GroupView> groups) {
}
