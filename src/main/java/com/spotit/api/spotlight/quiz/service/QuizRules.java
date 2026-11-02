package com.spotit.api.spotlight.quiz.service;

import com.spotit.api.spotlight.quiz.entity.QuizLevel;
import com.spotit.api.spotlight.quiz.entity.QuizQuestion;
import com.spotit.api.spotlight.quiz.entity.QuizSeen;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class QuizRules {

    private QuizRules() {
    }



    public static int currentLevel(Collection<Integer> clearedLevels, int levelCount) {
        int next = 1;
        while (clearedLevels.contains(next)) {
            next++;
        }
        return Math.max(1, Math.min(next, levelCount));
    }

    public static String levelState(int number, int current, boolean cleared) {
        if (cleared) {
            return "cleared";
        }
        return number == current ? "current" : "locked";
    }






    public static int freeLevels(Collection<List<QuizLevel>> levelsByTopic) {
        int fewest = Integer.MAX_VALUE;
        for (List<QuizLevel> levels : levelsByTopic) {
            int free = 0;
            for (QuizLevel level : levels.stream().sorted(Comparator.comparingInt(QuizLevel::getNumber)).toList()) {
                if (level.getCost() > 0) {
                    break;
                }
                free++;
            }
            fewest = Math.min(fewest, free);
        }
        return fewest == Integer.MAX_VALUE ? 0 : fewest;
    }




    public static int starsFor(int livesLeft, int startLives) {
        if (livesLeft <= 0) {
            return 0;
        }
        int lost = startLives - livesLeft;
        return Math.max(1, 3 - lost);
    }

    public static List<QuizQuestion> pickQuestions(List<QuizQuestion> pool, List<QuizSeen> seen, int rounds, Random random) {
        Map<String, Instant> seenAt = seen.stream().collect(Collectors.toMap(QuizSeen::getQuestionId, QuizSeen::getSeenAt, (a, b) -> a));
        List<QuizQuestion> fresh = new ArrayList<>(pool.stream().filter(q -> !seenAt.containsKey(q.getId())).toList());
        Collections.shuffle(fresh, random);
        List<QuizQuestion> old = pool.stream()
                .filter(q -> seenAt.containsKey(q.getId()))
                .sorted(Comparator.comparing((Function<QuizQuestion, Instant>) q -> seenAt.get(q.getId())).thenComparing(QuizQuestion::getId))
                .toList();
        List<QuizQuestion> picked = new ArrayList<>(fresh);
        picked.addAll(old);
        return picked.subList(0, Math.min(rounds, picked.size()));
    }
}
