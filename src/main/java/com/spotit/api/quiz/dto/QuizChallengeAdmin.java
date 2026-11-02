package com.spotit.api.quiz.dto;

import com.spotit.api.quiz.entity.QuizChallenge;

public record QuizChallengeAdmin(
        String id,
        String kind,
        String name,
        String blurb,
        String icon,
        String tone,
        Integer cost,
        Boolean costFrom,
        Integer minAge,
        String status,
        Integer sortOrder,
        Boolean active
) {
    public static QuizChallengeAdmin of(QuizChallenge c) {
        return new QuizChallengeAdmin(c.getId(), c.getKind(), c.getName(), c.getBlurb(), c.getIcon(), c.getTone(), c.getCost(),
                c.isCostFrom(), c.getMinAge(), c.getStatus(), c.getSortOrder(), c.isActive());
    }
}
