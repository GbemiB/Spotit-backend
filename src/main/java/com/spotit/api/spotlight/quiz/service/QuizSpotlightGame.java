package com.spotit.api.spotlight.quiz.service;

import com.spotit.api.spotlight.core.service.GameSummary;
import com.spotit.api.spotlight.core.service.SpotlightGame;
import com.spotit.api.spotlight.quiz.entity.QuizLevelResult;
import com.spotit.api.spotlight.quiz.repository.QuizAttemptRepository;
import com.spotit.api.spotlight.quiz.repository.QuizLevelResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuizSpotlightGame implements SpotlightGame {
    static final int HISTORY_DAYS = 400;
    private final QuizLevelResultRepository resultRepository;
    private final QuizAttemptRepository attemptRepository;

    @Override
    public String challengeId() {
        return "know-yourself";
    }

    @Override
    public GameSummary summary(UUID userId, LocalDate today) {
        List<QuizLevelResult> results = resultRepository.findByUserId(userId);
        int stars = results.stream().mapToInt(QuizLevelResult::getBestStars).sum();
        int won = (int) results.stream().filter(r -> r.getBestStars() > 0).count();
        int topics = (int) results.stream().map(QuizLevelResult::getTopicId).distinct().count();
        return new GameSummary(stars, won, attemptRepository.sumCorrect(userId), attemptRepository.maxBestStreak(userId), topics,
                new HashSet<>(attemptRepository.findPlayDaysSince(userId, today.minusDays(HISTORY_DAYS))), attemptRepository.countByUserId(userId));
    }
}
