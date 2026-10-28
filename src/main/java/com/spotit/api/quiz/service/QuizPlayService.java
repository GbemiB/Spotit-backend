package com.spotit.api.quiz.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.exception.ErrorMessage;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.quiz.dto.AnswerRequest;
import com.spotit.api.quiz.dto.AnswerResponse;
import com.spotit.api.quiz.dto.GroupView;
import com.spotit.api.quiz.dto.HintRequest;
import com.spotit.api.quiz.dto.HintResponse;
import com.spotit.api.quiz.dto.LevelView;
import com.spotit.api.quiz.dto.QuizHomeResponse;
import com.spotit.api.quiz.dto.QuizStats;
import com.spotit.api.quiz.dto.QuizTopicDetail;
import com.spotit.api.quiz.dto.QuizTopicsResponse;
import com.spotit.api.quiz.dto.RoundView;
import com.spotit.api.quiz.dto.StartQuizRequest;
import com.spotit.api.quiz.dto.StartQuizResponse;
import com.spotit.api.quiz.dto.TodaysPick;
import com.spotit.api.quiz.dto.TopicCard;
import com.spotit.api.quiz.entity.QuizAttempt;
import com.spotit.api.quiz.entity.QuizGroup;
import com.spotit.api.quiz.entity.QuizLevel;
import com.spotit.api.quiz.entity.QuizLevelResult;
import com.spotit.api.quiz.entity.QuizQuestion;
import com.spotit.api.quiz.entity.QuizSeen;
import com.spotit.api.quiz.entity.QuizTopic;
import com.spotit.api.quiz.repository.QuizAttemptRepository;
import com.spotit.api.quiz.repository.QuizGroupRepository;
import com.spotit.api.quiz.repository.QuizLevelRepository;
import com.spotit.api.quiz.repository.QuizLevelResultRepository;
import com.spotit.api.quiz.repository.QuizQuestionRepository;
import com.spotit.api.quiz.repository.QuizSeenRepository;
import com.spotit.api.quiz.repository.QuizTopicRepository;
import com.spotit.api.rewards.service.PointsWriteService;
import com.spotit.api.user.entity.User;
import com.spotit.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizPlayService {
    static final int HOME_TOPIC_CARDS = 6;
    static final String SPEND_ICON = "🎯";
    static final String HINT_ICON = "💡";
    private static final Random RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final UserClock userClock;
    private final ConfigurationDomainService config;
    private final PointsWriteService pointsWriteService;
    private final QuizGroupRepository groupRepository;
    private final QuizTopicRepository topicRepository;
    private final QuizLevelRepository levelRepository;
    private final QuizQuestionRepository questionRepository;
    private final QuizLevelResultRepository resultRepository;
    private final QuizAttemptRepository attemptRepository;
    private final QuizSeenRepository seenRepository;

    record Content(List<QuizGroup> groups, List<QuizTopic> topics, Map<String, List<QuizLevel>> levels, Map<String, Long> ready) {
        List<QuizLevel> levelsOf(String topicId) {
            return levels.getOrDefault(topicId, List.of());
        }

        boolean isReady(String topicId, int level, int rounds) {
            return ready.getOrDefault(topicId + "#" + level, 0L) >= rounds;
        }
    }

    record Player(User user, LocalDate today, Integer age, Map<String, Map<Integer, QuizLevelResult>> results) {
        Map<Integer, QuizLevelResult> of(String topicId) {
            return results.getOrDefault(topicId, Map.of());
        }

        Set<Integer> cleared(String topicId) {
            return of(topicId).values().stream().filter(r -> r.getBestStars() > 0).map(QuizLevelResult::getLevelNumber)
                    .collect(Collectors.toSet());
        }
    }

    @Transactional(readOnly = true)
    public QuizHomeResponse home(UUID userId) {
        Player player = player(userId);
        Content content = content();
        int rounds = config.getQuizRounds();
        List<TopicCard> cards = content.topics().stream().map(t -> card(t, content, player, rounds)).toList();
        List<TopicCard> open = cards.stream().filter(c -> !c.locked()).toList();
        List<TopicCard> shown = new ArrayList<>(open.stream().filter(TopicCard::started).toList());
        open.stream().filter(c -> !c.started()).forEach(shown::add);
        Set<LocalDate> days = new HashSet<>(attemptRepository.findPlayDaysSince(userId, player.today().minusDays(400)));
        List<QuizLevelResult> all = player.results().values().stream().flatMap(m -> m.values().stream()).toList();
        QuizStats stats = new QuizStats(QuizRules.streak(days, player.today()),
                all.stream().mapToInt(QuizLevelResult::getBestStars).sum(),
                (int) all.stream().filter(r -> r.getBestStars() > 0).count());
        return new QuizHomeResponse(player.user().getPoints(), stats, pick(userId, player, content, open), shown.stream().limit(HOME_TOPIC_CARDS).toList(),
                content.topics().size(), rounds, config.getQuizLives(), config.getQuizHintCost());
    }

    @Transactional(readOnly = true)
    public QuizTopicsResponse topics(UUID userId) {
        Player player = player(userId);
        Content content = content();
        int rounds = config.getQuizRounds();
        List<GroupView> groups = content.groups().stream()
                .map(g -> new GroupView(g.getId(), g.getLabel(), g.getTone(), content.topics().stream()
                        .filter(t -> t.getGroupId().equals(g.getId())).map(t -> card(t, content, player, rounds)).toList()))
                .filter(g -> !g.topics().isEmpty())
                .toList();
        boolean underAge = player.age() != null && player.age() < QuizRules.ADULT_AGE;
        return new QuizTopicsResponse(underAge, content.topics().size(), groups);
    }

    @Transactional(readOnly = true)
    public QuizTopicDetail topic(UUID userId, String topicId) {
        Player player = player(userId);
        Content content = content();
        QuizTopic topic = activeTopic(content, topicId);
        int rounds = config.getQuizRounds();
        List<QuizLevel> levels = content.levelsOf(topicId);
        Set<Integer> cleared = player.cleared(topicId);
        int current = QuizRules.currentLevel(cleared, levels.size());
        List<LevelView> views = levels.stream().map(l -> new LevelView(l.getNumber(), l.getName(), l.getCost(),
                QuizRules.levelState(l.getNumber(), current, cleared.contains(l.getNumber())),
                stars(player.of(topicId).get(l.getNumber())), content.isReady(topicId, l.getNumber(), rounds))).toList();
        return new QuizTopicDetail(card(topic, content, player, rounds), player.user().getPoints(), views);
    }

    @Transactional
    public StartQuizResponse start(UUID userId, StartQuizRequest request) {
        Player player = player(userId);
        Content content = content();
        QuizTopic topic = activeTopic(content, request.topicId());
        if (QuizRules.isLocked(topic.getMinAge(), player.age())) {
            throw new ApiException(ErrorCode.QUIZ_LOCKED, ErrorMessage.QUIZ_TOPIC_AGE_LOCKED);
        }
        List<QuizLevel> levels = content.levelsOf(topic.getId());
        QuizLevel level = levels.stream().filter(l -> l.getNumber() == request.level()).findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.QUIZ_LEVEL_NOT_FOUND));
        Set<Integer> cleared = player.cleared(topic.getId());
        if (!cleared.contains(level.getNumber()) && level.getNumber() != QuizRules.currentLevel(cleared, levels.size())) {
            throw new ApiException(ErrorCode.QUIZ_LOCKED, ErrorMessage.QUIZ_LEVEL_LOCKED);
        }
        int rounds = config.getQuizRounds();
        List<QuizQuestion> pool = questionRepository.findByTopicIdAndLevelNumberAndStatusAndActiveTrue(topic.getId(), level.getNumber(),
                QuizQuestion.APPROVED);
        if (pool.size() < rounds) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.QUIZ_LEVEL_NOT_READY);
        }
        long balance = player.user().getPoints();
        if (level.getCost() > 0) {
            if (balance < level.getCost()) {
                throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
            }
            balance = pointsWriteService.adjust(userId, -level.getCost(), SPEND_ICON,
                    "Quiz: " + topic.getName() + " level " + level.getNumber());
        }
        List<QuizSeen> seen = seenRepository.findByUserIdAndQuestionIdIn(userId, pool.stream().map(QuizQuestion::getId).toList());
        List<QuizQuestion> picked = QuizRules.pickQuestions(pool, seen, rounds, RANDOM);
        markSeen(userId, picked, seen);
        int lives = config.getQuizLives();
        QuizAttempt attempt = attemptRepository.save(QuizAttempt.builder()
                .userId(userId).topicId(topic.getId()).levelNumber(level.getNumber())
                .questionIds(picked.stream().map(QuizQuestion::getId).collect(Collectors.joining(",")))
                .round(0).lives(lives).hintRound(-1).spSpent(level.getCost())
                .status(QuizAttempt.PLAYING).playedOn(player.today()).startedAt(Instant.now())
                .build());
        QuizLevelResult result = player.of(topic.getId()).get(level.getNumber());
        if (result == null) {
            result = QuizLevelResult.builder().userId(userId).topicId(topic.getId()).levelNumber(level.getNumber()).build();
        }
        result.setPlays(result.getPlays() + 1);
        resultRepository.save(result);
        List<RoundView> roundViews = new ArrayList<>();
        for (int i = 0; i < picked.size(); i++) {
            roundViews.add(new RoundView(i, picked.get(i).getStatement()));
        }
        return new StartQuizResponse(attempt.getId(), topic.getId(), topic.getName(), level.getNumber(), level.getName(), roundViews, lives,
                config.getQuizHintCost(), level.getCost(), balance);
    }

    @Transactional
    public HintResponse hint(UUID userId, UUID attemptId, HintRequest request) {
        QuizAttempt attempt = playing(userId, attemptId, request.round());
        if (attempt.getHintRound() == attempt.getRound()) {
            throw new ApiException(ErrorCode.RESOURCE_ALREADY_EXISTS, ErrorMessage.QUIZ_HINT_USED);
        }
        QuizQuestion question = question(attempt);
        long balance = requireUser(userId).getPoints();
        if (question.getHint() == null || question.getHint().isBlank()) {
            return new HintResponse(attempt.getRound(), null, balance);
        }
        int cost = config.getQuizHintCost();
        if (balance < cost) {
            throw new ApiException(ErrorCode.INSUFFICIENT_POINTS, ErrorMessage.INSUFFICIENT_POINTS);
        }
        balance = pointsWriteService.adjust(userId, -cost, HINT_ICON, "Quiz hint");
        attempt.setHintRound(attempt.getRound());
        attempt.setHintsUsed(attempt.getHintsUsed() + 1);
        attempt.setSpSpent(attempt.getSpSpent() + cost);
        attemptRepository.save(attempt);
        return new HintResponse(attempt.getRound(), question.getHint(), balance);
    }

    @Transactional
    public AnswerResponse answer(UUID userId, UUID attemptId, AnswerRequest request) {
        QuizAttempt attempt = playing(userId, attemptId, request.round());
        QuizQuestion question = question(attempt);
        int round = attempt.getRound();
        boolean right = question.getAnswer().equals(request.choice());
        attempt.setStreak(right ? attempt.getStreak() + 1 : 0);
        attempt.setBestStreak(Math.max(attempt.getBestStreak(), attempt.getStreak()));
        attempt.setCorrect(attempt.getCorrect() + (right ? 1 : 0));
        attempt.setLives(right ? attempt.getLives() : attempt.getLives() - 1);
        int stars = 0;
        Integer nextLevel = null;
        if (attempt.getLives() <= 0) {
            finish(attempt, QuizAttempt.OUT);
        } else if (round + 1 >= attempt.questionIdList().size()) {
            finish(attempt, QuizAttempt.CLEARED);
            stars = QuizRules.starsFor(attempt.getLives(), config.getQuizLives());
            nextLevel = recordClear(userId, attempt, stars);
        } else {
            attempt.setRound(round + 1);
        }
        attemptRepository.save(attempt);
        return new AnswerResponse(round, right, question.getAnswer(), question.getExplanation(), question.getFunFact(), attempt.getLives(),
                attempt.getStreak(), attempt.getBestStreak(), attempt.getCorrect(), attempt.getStatus(), stars, nextLevel);
    }

    private Integer recordClear(UUID userId, QuizAttempt attempt, int stars) {
        QuizLevelResult result = resultRepository.findByUserIdAndTopicIdAndLevelNumber(userId, attempt.getTopicId(), attempt.getLevelNumber())
                .orElseGet(() -> QuizLevelResult.builder().userId(userId).topicId(attempt.getTopicId()).levelNumber(attempt.getLevelNumber())
                        .plays(1).build());
        boolean firstClear = result.getBestStars() == 0;
        result.setBestStars(Math.max(result.getBestStars(), stars));
        result.setClears(result.getClears() + 1);
        resultRepository.save(result);
        if (!firstClear) {
            return null;
        }
        boolean hasNext = levelRepository.findByTopicIdAndNumber(attempt.getTopicId(), attempt.getLevelNumber() + 1)
                .filter(QuizLevel::isActive).isPresent();
        return hasNext ? attempt.getLevelNumber() + 1 : null;
    }

    private static void finish(QuizAttempt attempt, String status) {
        attempt.setStatus(status);
        attempt.setFinishedAt(Instant.now());
    }

    private QuizAttempt playing(UUID userId, UUID attemptId, int round) {
        QuizAttempt attempt = attemptRepository.findById(attemptId).filter(a -> a.getUserId().equals(userId))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.QUIZ_ATTEMPT_NOT_FOUND));
        if (!QuizAttempt.PLAYING.equals(attempt.getStatus())) {
            throw new ApiException(ErrorCode.NOT_YET_COMPLETE, ErrorMessage.QUIZ_GAME_OVER);
        }
        if (attempt.getRound() != round) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, ErrorMessage.QUIZ_WRONG_ROUND);
        }
        return attempt;
    }

    private QuizQuestion question(QuizAttempt attempt) {
        String id = attempt.questionIdList().get(attempt.getRound());
        return questionRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.QUIZ_QUESTION_NOT_FOUND));
    }

    private void markSeen(UUID userId, List<QuizQuestion> picked, List<QuizSeen> seen) {
        Map<String, QuizSeen> byId = seen.stream().collect(Collectors.toMap(QuizSeen::getQuestionId, s -> s, (a, b) -> a));
        Instant now = Instant.now();
        List<QuizSeen> rows = new ArrayList<>();
        for (int i = 0; i < picked.size(); i++) {
            String id = picked.get(i).getId();
            QuizSeen row = byId.getOrDefault(id, QuizSeen.builder().userId(userId).questionId(id).build());
            row.setSeenAt(now.plusMillis(i));
            rows.add(row);
        }
        seenRepository.saveAll(rows);
    }

    private TodaysPick pick(UUID userId, Player player, Content content, List<TopicCard> open) {
        List<TopicCard> ready = open.stream().filter(TopicCard::ready).toList();
        List<TopicCard> started = ready.stream().filter(TopicCard::started).filter(c -> c.levelsCleared() < c.levelCount()).toList();
        List<TopicCard> pool = started.isEmpty() ? ready : started;
        if (pool.isEmpty()) {
            return null;
        }
        TopicCard card = pool.get(Math.floorMod(Objects.hash(userId, player.today()), pool.size()));
        QuizLevel level = content.levelsOf(card.id()).stream().filter(l -> l.getNumber() == card.currentLevel()).findFirst().orElseThrow();
        return new TodaysPick(card.id(), card.name(), card.groupId(), card.tone(), level.getNumber(), level.getName(), level.getCost());
    }

    private TopicCard card(QuizTopic topic, Content content, Player player, int rounds) {
        List<QuizLevel> levels = content.levelsOf(topic.getId());
        Set<Integer> cleared = player.cleared(topic.getId());
        int current = QuizRules.currentLevel(cleared, levels.size());
        String tone = content.groups().stream().filter(g -> g.getId().equals(topic.getGroupId())).map(QuizGroup::getTone).findFirst().orElse("pink");
        return new TopicCard(topic.getId(), topic.getName(), topic.getGroupId(), tone, topic.getMinAge(),
                QuizRules.isLocked(topic.getMinAge(), player.age()), !player.of(topic.getId()).isEmpty(), current, levels.size(), cleared.size(),
                content.isReady(topic.getId(), current, rounds));
    }

    private static int stars(QuizLevelResult result) {
        return result == null ? 0 : result.getBestStars();
    }

    private QuizTopic activeTopic(Content content, String topicId) {
        return content.topics().stream().filter(t -> t.getId().equals(topicId)).findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.QUIZ_TOPIC_NOT_FOUND));
    }

    private Content content() {
        List<QuizGroup> groups = groupRepository.findAllByOrderBySortOrderAsc().stream().filter(QuizGroup::isActive).toList();
        Set<String> groupIds = groups.stream().map(QuizGroup::getId).collect(Collectors.toSet());
        List<QuizTopic> topics = topicRepository.findByActiveTrueOrderBySortOrderAsc().stream().filter(t -> groupIds.contains(t.getGroupId())).toList();
        Map<String, List<QuizLevel>> levels = levelRepository.findByActiveTrueOrderByTopicIdAscNumberAsc().stream()
                .collect(Collectors.groupingBy(QuizLevel::getTopicId));
        Map<String, Long> ready = new HashMap<>();
        for (Object[] row : questionRepository.countReady()) {
            ready.put(row[0] + "#" + row[1], ((Number) row[2]).longValue());
        }
        return new Content(groups, topics, levels, ready);
    }

    private Player player(UUID userId) {
        User user = requireUser(userId);
        LocalDate today = userClock.today(user);
        Map<String, Map<Integer, QuizLevelResult>> results = new HashMap<>();
        for (QuizLevelResult r : resultRepository.findByUserId(userId)) {
            results.computeIfAbsent(r.getTopicId(), k -> new HashMap<>()).put(r.getLevelNumber(), r);
        }
        return new Player(user, today, QuizRules.ageOn(user.getDob(), today), results);
    }

    private User requireUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
    }
}
