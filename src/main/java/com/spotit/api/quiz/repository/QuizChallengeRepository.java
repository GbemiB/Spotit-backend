package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizChallengeRepository extends JpaRepository<QuizChallenge, String> {
    List<QuizChallenge> findAllByOrderBySortOrderAsc();

    List<QuizChallenge> findByActiveTrueOrderBySortOrderAsc();
}
