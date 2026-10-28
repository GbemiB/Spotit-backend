package com.spotit.api.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "quiz_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {
    public static final String MYTH = "myth";
    public static final String FACT = "fact";
    public static final String DRAFT = "draft";
    public static final String APPROVED = "approved";

    @Id
    @Column(length = 40)
    private String id;

    @Column(name = "topic_id", nullable = false, length = 40)
    private String topicId;

    @Column(name = "level_number", nullable = false)
    private int levelNumber;

    @Column(nullable = false, length = 300)
    private String statement;

    @Column(nullable = false, length = 4)
    private String answer;

    @Column(length = 300)
    private String hint;

    @Column(nullable = false, length = 600)
    private String explanation;

    @Column(name = "fun_fact", length = 400)
    private String funFact;

    @Column(length = 300)
    private String source;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(name = "reviewed_by", length = 120)
    private String reviewedBy;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = Instant.now();
    }
}
