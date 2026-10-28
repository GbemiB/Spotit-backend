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

@Entity
@Table(name = "quiz_levels", uniqueConstraints = @UniqueConstraint(name = "uq_quiz_level", columnNames = {"topic_id", "number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_id", nullable = false, length = 40)
    private String topicId;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private boolean active;
}
