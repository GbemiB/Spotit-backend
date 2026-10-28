package com.spotit.api.quiz.dto;

import com.spotit.api.quiz.entity.QuizTopic;

public record QuizTopicAdmin(String id, String name, String groupId, Integer minAge, Integer sortOrder, Boolean active) {
    public static QuizTopicAdmin of(QuizTopic t) {
        return new QuizTopicAdmin(t.getId(), t.getName(), t.getGroupId(), t.getMinAge(), t.getSortOrder(), t.isActive());
    }
}
