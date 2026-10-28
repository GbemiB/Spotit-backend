package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizLevelResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizLevelResultRepository extends JpaRepository<QuizLevelResult, UUID> {
    List<QuizLevelResult> findByUserId(UUID userId);

    Optional<QuizLevelResult> findByUserIdAndTopicIdAndLevelNumber(UUID userId, String topicId, int levelNumber);
}
