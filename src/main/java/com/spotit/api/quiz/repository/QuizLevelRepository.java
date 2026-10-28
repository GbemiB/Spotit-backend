package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizLevelRepository extends JpaRepository<QuizLevel, Long> {
    List<QuizLevel> findAllByOrderByTopicIdAscNumberAsc();

    List<QuizLevel> findByTopicIdOrderByNumberAsc(String topicId);

    List<QuizLevel> findByActiveTrueOrderByTopicIdAscNumberAsc();

    Optional<QuizLevel> findByTopicIdAndNumber(String topicId, int number);
}
