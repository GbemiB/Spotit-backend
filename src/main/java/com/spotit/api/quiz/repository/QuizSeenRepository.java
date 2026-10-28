package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizSeen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface QuizSeenRepository extends JpaRepository<QuizSeen, UUID> {
    List<QuizSeen> findByUserIdAndQuestionIdIn(UUID userId, Collection<String> questionIds);
}
