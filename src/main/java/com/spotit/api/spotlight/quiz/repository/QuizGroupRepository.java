package com.spotit.api.spotlight.quiz.repository;

import com.spotit.api.spotlight.quiz.entity.QuizGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizGroupRepository extends JpaRepository<QuizGroup, String> {
    List<QuizGroup> findAllByOrderBySortOrderAsc();
}
