package com.spotit.api.quiz.controller;

import com.spotit.api.common.security.CurrentUserId;
import com.spotit.api.quiz.dto.AnswerRequest;
import com.spotit.api.quiz.dto.AnswerResponse;
import com.spotit.api.quiz.dto.HintRequest;
import com.spotit.api.quiz.dto.HintResponse;
import com.spotit.api.quiz.dto.QuizHomeResponse;
import com.spotit.api.quiz.dto.QuizTopicDetail;
import com.spotit.api.quiz.dto.QuizTopicsResponse;
import com.spotit.api.quiz.dto.StartQuizRequest;
import com.spotit.api.quiz.dto.StartQuizResponse;
import com.spotit.api.quiz.service.QuizPlayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Know Yourself quiz", description = "The Spotlight myth-or-fact quiz: topics, levels, games, hints and answers. "
        + "Answers are marked on the server; a game never sends the answers ahead.")
@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizPlayService quizPlayService;

    @Operation(summary = "Spotlight hub", description = "SpotPoints balance, quiz streak, stars, levels cleared, today's pick and up to six "
            + "topic cards (started topics first). pick is null when no level has enough approved questions yet.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/home")
    public QuizHomeResponse home(@CurrentUserId UUID userId) {
        return quizPlayService.home(userId);
    }

    @Operation(summary = "All topics", description = "Every active topic by group. 18+ topics come back locked for under-18s, using the "
            + "date of birth. ready is false when the topic's current level has too few approved questions.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/topics")
    public QuizTopicsResponse topics(@CurrentUserId UUID userId) {
        return quizPlayService.topics(userId);
    }

    @Operation(summary = "Level map for a topic", description = "Each level with its name, SP cost, state (cleared, current or locked), "
            + "best stars and whether it has enough approved questions.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/topics/{topicId}")
    public QuizTopicDetail topic(@CurrentUserId UUID userId,
                                 @Parameter(description = "Topic id", example = "my-cycle") @PathVariable String topicId) {
        return quizPlayService.topic(userId, topicId);
    }

    @Operation(summary = "Start a level", description = "Starts a game on a cleared or current level. Paid levels take their SP cost "
            + "every time they are played (402 when the balance is short). Returns the statements only, never the answers. "
            + "403 for an 18+ topic under 18 or a locked level; 409 when the level has too few approved questions.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/attempts")
    public StartQuizResponse start(@CurrentUserId UUID userId, @Valid @RequestBody StartQuizRequest request) {
        return quizPlayService.start(userId, request);
    }

    @Operation(summary = "Take a hint", description = "Charges the hint cost and returns the hint for the round being played. One hint "
            + "per round; no charge when the question has no hint.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/attempts/{attemptId}/hint")
    public HintResponse hint(@CurrentUserId UUID userId, @PathVariable UUID attemptId, @Valid @RequestBody HintRequest request) {
        return quizPlayService.hint(userId, attemptId, request);
    }

    @Operation(summary = "Answer a round", description = "choice is myth or fact. Returns whether it was right, the answer, explanation, "
            + "fun fact, hearts left and streak. status becomes cleared (with 1 to 3 stars and nextLevelOpened on a first clear) or out "
            + "when the game ends.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/attempts/{attemptId}/answers")
    public AnswerResponse answer(@CurrentUserId UUID userId, @PathVariable UUID attemptId, @Valid @RequestBody AnswerRequest request) {
        return quizPlayService.answer(userId, attemptId, request);
    }
}
