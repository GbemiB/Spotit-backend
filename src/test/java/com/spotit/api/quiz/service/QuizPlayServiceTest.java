package com.spotit.api.quiz.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.common.time.UserClock;
import com.spotit.api.configuration.service.ConfigurationDomainService;
import com.spotit.api.quiz.dto.AnswerRequest;
import com.spotit.api.quiz.dto.AnswerResponse;
import com.spotit.api.quiz.dto.HintRequest;
import com.spotit.api.quiz.dto.QuizHomeResponse;
import com.spotit.api.quiz.dto.QuizTopicDetail;
import com.spotit.api.quiz.dto.QuizTopicsResponse;
import com.spotit.api.quiz.dto.StartQuizRequest;
import com.spotit.api.quiz.dto.StartQuizResponse;
import com.spotit.api.quiz.entity.QuizAttempt;
import com.spotit.api.quiz.entity.QuizGroup;
import com.spotit.api.quiz.entity.QuizLevel;
import com.spotit.api.quiz.entity.QuizLevelResult;
import com.spotit.api.quiz.entity.QuizQuestion;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuizPlayServiceTest {
    @Mock UserRepository userRepository;
    @Mock UserClock userClock;
    @Mock ConfigurationDomainService config;
    @Mock PointsWriteService pointsWriteService;
    @Mock QuizGroupRepository groupRepository;
    @Mock QuizTopicRepository topicRepository;
    @Mock QuizLevelRepository levelRepository;
    @Mock QuizQuestionRepository questionRepository;
    @Mock QuizLevelResultRepository resultRepository;
    @Mock QuizAttemptRepository attemptRepository;
    @Mock QuizSeenRepository seenRepository;
    @InjectMocks QuizPlayService service;

    final UUID userId = UUID.randomUUID();
    final LocalDate today = LocalDate.of(2026, 10, 28);
    final List<QuizLevelResult> results = new ArrayList<>();
    User user;

    static QuizQuestion question(String id, int level, String answer, String hint) {
        return QuizQuestion.builder().id(id).topicId("my-cycle").levelNumber(level).statement("Statement " + id).answer(answer).hint(hint)
                .explanation("Because " + id).funFact("Fun " + id).status(QuizQuestion.APPROVED).active(true).build();
    }

    static QuizLevel level(String topic, int number, int cost) {
        return QuizLevel.builder().topicId(topic).number(number).name("Level " + number).cost(cost).active(true).build();
    }

    @BeforeEach
    void setUp() {
        user = User.builder().id(userId).points(100).dob(LocalDate.of(2000, 1, 1)).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userClock.today(user)).thenReturn(today);
        when(config.getQuizRounds()).thenReturn(2);
        when(config.getQuizLives()).thenReturn(3);
        when(config.getQuizHintCost()).thenReturn(5);
        when(groupRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(
                QuizGroup.builder().id("body").label("Body basics").tone("pink").sortOrder(1).active(true).build(),
                QuizGroup.builder().id("rel").label("Relationships").tone("gold").sortOrder(2).active(true).build(),
                QuizGroup.builder().id("off").label("Hidden").tone("green").sortOrder(3).active(false).build()));
        when(topicRepository.findByActiveTrueOrderBySortOrderAsc()).thenReturn(List.of(
                QuizTopic.builder().id("my-cycle").name("My Cycle").groupId("body").minAge(13).sortOrder(1).active(true).build(),
                QuizTopic.builder().id("period-myths").name("Period Myths").groupId("body").minAge(13).sortOrder(2).active(true).build(),
                QuizTopic.builder().id("sex").name("Sex & Intimacy").groupId("rel").minAge(18).sortOrder(3).active(true).build(),
                QuizTopic.builder().id("ghost").name("Ghost").groupId("off").minAge(13).sortOrder(4).active(true).build()));
        when(levelRepository.findByActiveTrueOrderByTopicIdAscNumberAsc()).thenReturn(List.of(
                level("my-cycle", 1, 0), level("my-cycle", 2, 30), level("period-myths", 1, 0), level("sex", 1, 0)));
        when(questionRepository.countReady()).thenReturn(List.of(
                new Object[]{"my-cycle", 1, 2L}, new Object[]{"my-cycle", 2, 2L}, new Object[]{"sex", 1, 2L}));
        when(resultRepository.findByUserId(userId)).thenReturn(results);
        when(attemptRepository.findPlayDaysSince(eq(userId), any())).thenReturn(List.of(today, today.minusDays(1)));
        when(attemptRepository.save(any())).thenAnswer(inv -> {
            QuizAttempt a = inv.getArgument(0);
            if (a.getId() == null) {
                a.setId(UUID.randomUUID());
            }
            return a;
        });
        when(questionRepository.findByTopicIdAndLevelNumberAndStatusAndActiveTrue("my-cycle", 1, QuizQuestion.APPROVED))
                .thenReturn(List.of(question("q1", 1, "myth", "Hint one"), question("q2", 1, "fact", null)));
        when(questionRepository.findByTopicIdAndLevelNumberAndStatusAndActiveTrue("my-cycle", 2, QuizQuestion.APPROVED))
                .thenReturn(List.of(question("q3", 2, "fact", "Hint three"), question("q4", 2, "myth", "Hint four")));
        when(questionRepository.findById("q1")).thenReturn(Optional.of(question("q1", 1, "myth", "Hint one")));
        when(questionRepository.findById("q2")).thenReturn(Optional.of(question("q2", 1, "fact", null)));
        when(seenRepository.findByUserIdAndQuestionIdIn(eq(userId), any())).thenReturn(List.of());
        when(levelRepository.findByTopicIdAndNumber("my-cycle", 2)).thenReturn(Optional.of(level("my-cycle", 2, 30)));
        when(levelRepository.findByTopicIdAndNumber("my-cycle", 3)).thenReturn(Optional.empty());
    }

    void cleared(String topic, int level, int stars) {
        results.add(QuizLevelResult.builder().userId(userId).topicId(topic).levelNumber(level).bestStars(stars).plays(1).clears(1).build());
    }

    QuizAttempt attempt(String ids, int round, int lives) {
        QuizAttempt a = QuizAttempt.builder().id(UUID.randomUUID()).userId(userId).topicId("my-cycle").levelNumber(1).questionIds(ids)
                .round(round).lives(lives).hintRound(-1).status(QuizAttempt.PLAYING).playedOn(today).build();
        when(attemptRepository.findById(a.getId())).thenReturn(Optional.of(a));
        return a;
    }

    @Test
    void theHubShowsStartedTopicsFirstAndHidesAdultTopicsFromTeens() {
        user.setDob(LocalDate.of(2011, 1, 1));
        cleared("period-myths", 1, 2);

        QuizHomeResponse home = service.home(userId);

        assertThat(home.balance()).isEqualTo(100);
        assertThat(home.topics()).extracting("id").containsExactly("period-myths", "my-cycle");
        assertThat(home.topicCount()).isEqualTo(3);
        assertThat(home.stats().streak()).isEqualTo(2);
        assertThat(home.stats().stars()).isEqualTo(2);
        assertThat(home.stats().levelsDone()).isEqualTo(1);
        assertThat(home.pick().topicId()).isEqualTo("my-cycle");
        assertThat(home.rounds()).isEqualTo(2);
        assertThat(home.hintCost()).isEqualTo(5);
    }

    @Test
    void thePickPrefersATopicAlreadyStarted() {
        cleared("my-cycle", 1, 3);

        QuizHomeResponse home = service.home(userId);

        assertThat(home.pick().topicId()).isEqualTo("my-cycle");
        assertThat(home.pick().level()).isEqualTo(2);
        assertThat(home.pick().cost()).isEqualTo(30);
    }

    @Test
    void thereIsNoPickWhenNoLevelHasEnoughQuestions() {
        when(questionRepository.countReady()).thenReturn(List.of());

        assertThat(service.home(userId).pick()).isNull();
    }

    @Test
    void topicsComeByGroupWithAdultTopicsLockedForTeens() {
        user.setDob(LocalDate.of(2011, 1, 1));

        QuizTopicsResponse topics = service.topics(userId);

        assertThat(topics.underAge()).isTrue();
        assertThat(topics.groups()).extracting("id").containsExactly("body", "rel");
        assertThat(topics.groups().get(1).topics().get(0).locked()).isTrue();
        assertThat(topics.groups().get(0).topics().get(1).ready()).isFalse();
    }

    @Test
    void theLevelMapShowsStateStarsAndReadiness() {
        cleared("my-cycle", 1, 3);

        QuizTopicDetail detail = service.topic(userId, "my-cycle");

        assertThat(detail.levels()).extracting("state").containsExactly("cleared", "current");
        assertThat(detail.levels().get(0).stars()).isEqualTo(3);
        assertThat(detail.levels().get(1).cost()).isEqualTo(30);
        assertThat(detail.topic().levelsCleared()).isEqualTo(1);
        assertThat(detail.balance()).isEqualTo(100);
    }

    @Test
    void anUnknownTopicIs404() {
        assertThatThrownBy(() -> service.topic(userId, "nope")).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void aFreeLevelStartsWithoutChargingAndNeverSendsAnswers() {
        StartQuizResponse start = service.start(userId, new StartQuizRequest("my-cycle", 1));

        assertThat(start.rounds()).hasSize(2);
        assertThat(start.rounds().get(0).statement()).startsWith("Statement");
        assertThat(start.spSpent()).isZero();
        assertThat(start.balance()).isEqualTo(100);
        assertThat(start.lives()).isEqualTo(3);
        verify(pointsWriteService, never()).adjust(any(), anyInt(), anyString(), anyString());
        verify(seenRepository).saveAll(any());
        verify(resultRepository).save(any());
    }

    @Test
    void aPaidLevelChargesEveryTime() {
        cleared("my-cycle", 1, 3);
        when(pointsWriteService.adjust(userId, -30, QuizPlayService.SPEND_ICON, "Quiz: My Cycle level 2")).thenReturn(70L);

        StartQuizResponse start = service.start(userId, new StartQuizRequest("my-cycle", 2));

        assertThat(start.balance()).isEqualTo(70);
        assertThat(start.spSpent()).isEqualTo(30);
        assertThat(start.lives()).isEqualTo(3);
    }

    @Test
    void aHardLevelCanStartWithFewerHearts() {
        cleared("my-cycle", 1, 3);
        QuizLevel hard = level("my-cycle", 2, 30);
        hard.setLives(1);
        when(levelRepository.findByActiveTrueOrderByTopicIdAscNumberAsc()).thenReturn(List.of(level("my-cycle", 1, 0), hard));
        when(pointsWriteService.adjust(userId, -30, QuizPlayService.SPEND_ICON, "Quiz: My Cycle level 2")).thenReturn(70L);

        assertThat(service.topic(userId, "my-cycle").levels()).extracting("lives").containsExactly(3, 1);
        StartQuizResponse start = service.start(userId, new StartQuizRequest("my-cycle", 2));

        assertThat(start.lives()).isEqualTo(1);
        verify(attemptRepository).save(argThat(a -> a.getStartLives() == 1 && a.getLives() == 1));
    }

    @Test
    void clearingWithEveryHeartLeftOnAOneHeartLevelGivesThreeStars() {
        QuizAttempt a = attempt("q1,q2", 1, 1);
        a.setStartLives(1);
        when(resultRepository.findByUserIdAndTopicIdAndLevelNumber(userId, "my-cycle", 1)).thenReturn(Optional.empty());

        AnswerResponse answer = service.answer(userId, a.getId(), new AnswerRequest(1, "fact"));

        assertThat(answer.stars()).isEqualTo(3);
    }

    @Test
    void aPaidLevelNeedsEnoughPoints() {
        cleared("my-cycle", 1, 3);
        user.setPoints(10);

        assertThatThrownBy(() -> service.start(userId, new StartQuizRequest("my-cycle", 2))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
    }

    @Test
    void teensCannotStartAdultTopics() {
        user.setDob(LocalDate.of(2011, 1, 1));

        assertThatThrownBy(() -> service.start(userId, new StartQuizRequest("sex", 1))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.QUIZ_LOCKED);
    }

    @Test
    void levelsOpenInOrder() {
        assertThatThrownBy(() -> service.start(userId, new StartQuizRequest("my-cycle", 2))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.QUIZ_LOCKED);
    }

    @Test
    void aLevelWithoutEnoughQuestionsCannotStart() {
        when(questionRepository.findByTopicIdAndLevelNumberAndStatusAndActiveTrue("period-myths", 1, QuizQuestion.APPROVED)).thenReturn(List.of());

        assertThatThrownBy(() -> service.start(userId, new StartQuizRequest("period-myths", 1))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
    }

    @Test
    void anUnknownLevelIs404() {
        assertThatThrownBy(() -> service.start(userId, new StartQuizRequest("my-cycle", 9))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void aHintIsChargedOncePerRound() {
        QuizAttempt a = attempt("q1,q2", 0, 3);
        when(pointsWriteService.adjust(userId, -5, QuizPlayService.HINT_ICON, "Quiz hint")).thenReturn(95L);

        var hint = service.hint(userId, a.getId(), new HintRequest(0));

        assertThat(hint.hint()).isEqualTo("Hint one");
        assertThat(hint.balance()).isEqualTo(95);
        assertThat(a.getSpSpent()).isEqualTo(5);
        assertThatThrownBy(() -> service.hint(userId, a.getId(), new HintRequest(0))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.RESOURCE_ALREADY_EXISTS);
    }

    @Test
    void aQuestionWithoutAHintCostsNothing() {
        QuizAttempt a = attempt("q2,q1", 0, 3);

        var hint = service.hint(userId, a.getId(), new HintRequest(0));

        assertThat(hint.hint()).isNull();
        verify(pointsWriteService, never()).adjust(any(), anyInt(), anyString(), anyString());
    }

    @Test
    void aHintNeedsEnoughPoints() {
        user.setPoints(2);
        QuizAttempt a = attempt("q1,q2", 0, 3);

        assertThatThrownBy(() -> service.hint(userId, a.getId(), new HintRequest(0))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
    }

    @Test
    void aRightAnswerMovesToTheNextRound() {
        QuizAttempt a = attempt("q1,q2", 0, 3);

        AnswerResponse answer = service.answer(userId, a.getId(), new AnswerRequest(0, "myth"));

        assertThat(answer.right()).isTrue();
        assertThat(answer.explanation()).isEqualTo("Because q1");
        assertThat(answer.status()).isEqualTo(QuizAttempt.PLAYING);
        assertThat(a.getRound()).isEqualTo(1);
        assertThat(a.getStreak()).isEqualTo(1);
    }

    @Test
    void clearingTheLastRoundGivesStarsAndOpensTheNextLevel() {
        QuizAttempt a = attempt("q1,q2", 1, 2);
        when(resultRepository.findByUserIdAndTopicIdAndLevelNumber(userId, "my-cycle", 1)).thenReturn(Optional.empty());

        AnswerResponse answer = service.answer(userId, a.getId(), new AnswerRequest(1, "fact"));

        assertThat(answer.status()).isEqualTo(QuizAttempt.CLEARED);
        assertThat(answer.stars()).isEqualTo(2);
        assertThat(answer.nextLevelOpened()).isEqualTo(2);
        assertThat(a.getFinishedAt()).isNotNull();
    }

    @Test
    void replayingAClearedLevelKeepsTheBestStarsAndOpensNothingNew() {
        QuizAttempt a = attempt("q1,q2", 1, 1);
        QuizLevelResult result = QuizLevelResult.builder().userId(userId).topicId("my-cycle").levelNumber(1).bestStars(3).clears(1).build();
        when(resultRepository.findByUserIdAndTopicIdAndLevelNumber(userId, "my-cycle", 1)).thenReturn(Optional.of(result));

        AnswerResponse answer = service.answer(userId, a.getId(), new AnswerRequest(1, "fact"));

        assertThat(answer.stars()).isEqualTo(1);
        assertThat(answer.nextLevelOpened()).isNull();
        assertThat(result.getBestStars()).isEqualTo(3);
        assertThat(result.getClears()).isEqualTo(2);
    }

    @Test
    void losingTheLastHeartEndsTheGame() {
        QuizAttempt a = attempt("q1,q2", 0, 1);

        AnswerResponse answer = service.answer(userId, a.getId(), new AnswerRequest(0, "fact"));

        assertThat(answer.right()).isFalse();
        assertThat(answer.answer()).isEqualTo("myth");
        assertThat(answer.status()).isEqualTo(QuizAttempt.OUT);
        assertThat(answer.stars()).isZero();
    }

    @Test
    void answersMustBeForTheRoundBeingPlayed() {
        QuizAttempt a = attempt("q1,q2", 0, 3);

        assertThatThrownBy(() -> service.answer(userId, a.getId(), new AnswerRequest(1, "myth"))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void aFinishedGameTakesNoMoreAnswers() {
        QuizAttempt a = attempt("q1,q2", 0, 3);
        a.setStatus(QuizAttempt.OUT);

        assertThatThrownBy(() -> service.answer(userId, a.getId(), new AnswerRequest(0, "myth"))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_YET_COMPLETE);
    }

    @Test
    void someoneElsesGameIs404() {
        QuizAttempt a = attempt("q1,q2", 0, 3);
        a.setUserId(UUID.randomUUID());

        assertThatThrownBy(() -> service.answer(userId, a.getId(), new AnswerRequest(0, "myth"))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }

    @Test
    void anUnknownUserIs404() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.home(userId)).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }
}
