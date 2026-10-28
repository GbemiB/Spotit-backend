package com.spotit.api.quiz.dto;

import java.util.List;

public record QuizTopicDetail(TopicCard topic, long balance, List<LevelView> levels) {
}
