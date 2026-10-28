package com.spotit.api.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "quiz_level_results",
        uniqueConstraints = @UniqueConstraint(name = "uq_quiz_level_result", columnNames = {"user_id", "topic_id", "level_number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizLevelResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "topic_id", nullable = false, length = 40)
    private String topicId;

    @Column(name = "level_number", nullable = false)
    private int levelNumber;

    @Column(name = "best_stars", nullable = false)
    private int bestStars;

    @Column(nullable = false)
    private int plays;

    @Column(nullable = false)
    private int clears;
}
