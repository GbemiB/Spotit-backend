package com.spotit.api.spotlight.quiz.dto;

import java.util.List;

public record QuizTopicDetail(TopicCard topic, long balance, List<LevelView> levels) {
}
