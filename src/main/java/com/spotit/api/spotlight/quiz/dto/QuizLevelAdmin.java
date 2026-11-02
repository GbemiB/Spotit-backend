package com.spotit.api.spotlight.quiz.dto;

import com.spotit.api.spotlight.quiz.entity.QuizLevel;

public record QuizLevelAdmin(String topicId, Integer number, String name, Integer cost, Boolean active, Integer lives) {
    public static QuizLevelAdmin of(QuizLevel l) {
        return new QuizLevelAdmin(l.getTopicId(), l.getNumber(), l.getName(), l.getCost(), l.isActive(), l.getLives());
    }
}
