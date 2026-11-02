package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRankRepository extends JpaRepository<QuizRank, String> {
    List<QuizRank> findAllByOrderByMinStarsAsc();

    List<QuizRank> findByActiveTrueOrderByMinStarsAsc();
}
