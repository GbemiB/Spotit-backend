package com.spotit.api.quiz.repository;

import com.spotit.api.quiz.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, String> {
    List<QuizQuestion> findByTopicIdAndLevelNumberAndStatusAndActiveTrue(String topicId, int levelNumber, String status);

    List<QuizQuestion> findAllByOrderByTopicIdAscLevelNumberAscIdAsc();

    List<QuizQuestion> findByTopicIdOrderByLevelNumberAscIdAsc(String topicId);

    List<QuizQuestion> findByStatusOrderByTopicIdAscLevelNumberAscIdAsc(String status);

    List<QuizQuestion> findByTopicIdAndStatusOrderByLevelNumberAscIdAsc(String topicId, String status);

    @Query("SELECT q.topicId, q.levelNumber, COUNT(q) FROM QuizQuestion q WHERE q.status = 'approved' AND q.active = true "
            + "GROUP BY q.topicId, q.levelNumber")
    List<Object[]> countReady();
}
