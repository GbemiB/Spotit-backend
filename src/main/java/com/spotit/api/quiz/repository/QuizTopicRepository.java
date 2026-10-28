package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizTopicRepository extends JpaRepository<QuizTopic, String> {
    List<QuizTopic> findAllByOrderBySortOrderAsc();

    List<QuizTopic> findByActiveTrueOrderBySortOrderAsc();
}
