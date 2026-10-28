package com.spotit.api.quiz.controller;

import com.spotit.api.quiz.dto.ApproveQuestionRequest;
import com.spotit.api.quiz.dto.QuizGroupAdmin;
import com.spotit.api.quiz.dto.QuizLevelAdmin;
import com.spotit.api.quiz.dto.QuizQuestionAdmin;
import com.spotit.api.quiz.dto.QuizTopicAdmin;
import com.spotit.api.quiz.service.QuizAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Quiz Config (Admin)", description = "Groups, topics, levels and questions for the Know Yourself quiz. PUT creates or "
        + "updates; only the fields sent change, and active=false switches an item off. Any change to a question's wording or answer "
        + "puts it back to draft; players only ever get approved questions.")
@RestController
@RequestMapping("/api/v1/config/quiz")
@RequiredArgsConstructor
public class QuizConfigController {
    private final QuizAdminService quizAdminService;

    @Operation(summary = "List groups")
    @GetMapping("/groups")
    public List<QuizGroupAdmin> groups() {
        return quizAdminService.groups();
    }

    @Operation(summary = "Create or update a group", description = "tone is pink, peach, green or gold.")
    @PutMapping("/groups/{id}")
    public QuizGroupAdmin saveGroup(@Parameter(example = "body") @PathVariable String id, @RequestBody QuizGroupAdmin body) {
        return quizAdminService.saveGroup(id, body);
    }

    @Operation(summary = "List topics")
    @GetMapping("/topics")
    public List<QuizTopicAdmin> topics() {
        return quizAdminService.topics();
    }

    @Operation(summary = "Create or update a topic", description = "minAge is 13 or 18; 18+ topics are locked for under-18s.")
    @PutMapping("/topics/{id}")
    public QuizTopicAdmin saveTopic(@Parameter(example = "my-cycle") @PathVariable String id, @RequestBody QuizTopicAdmin body) {
        return quizAdminService.saveTopic(id, body);
    }

    @Operation(summary = "List levels", description = "All levels, or one topic's with ?topicId=.")
    @GetMapping("/levels")
    public List<QuizLevelAdmin> levels(@RequestParam(required = false) String topicId) {
        return quizAdminService.levels(topicId);
    }

    @Operation(summary = "Create or update a level", description = "cost is the SP charged each time the level is played; 0 is free.")
    @PutMapping("/levels/{topicId}/{number}")
    public QuizLevelAdmin saveLevel(@PathVariable String topicId, @PathVariable int number, @RequestBody QuizLevelAdmin body) {
        return quizAdminService.saveLevel(topicId, number, body);
    }

    @Operation(summary = "List questions", description = "Filter with ?topicId= and/or ?status=draft|approved.")
    @GetMapping("/questions")
    public List<QuizQuestionAdmin> questions(@RequestParam(required = false) String topicId, @RequestParam(required = false) String status) {
        return quizAdminService.questions(topicId, status);
    }

    @Operation(summary = "Create or update a question", description = "answer is myth or fact. New and edited questions are drafts.")
    @PutMapping("/questions/{id}")
    public QuizQuestionAdmin saveQuestion(@Parameter(example = "my-cycle-1-01") @PathVariable String id, @RequestBody QuizQuestionAdmin body) {
        return quizAdminService.saveQuestion(id, body);
    }

    @Operation(summary = "Approve a question", description = "Records who reviewed it; approved questions can be served to players.")
    @PostMapping("/questions/{id}/approve")
    public QuizQuestionAdmin approve(@PathVariable String id, @Valid @RequestBody ApproveQuestionRequest request) {
        return quizAdminService.approve(id, request.reviewedBy());
    }
}
