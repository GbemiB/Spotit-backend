package com.spotit.api.quiz.controller;

import com.spotit.api.quiz.dto.AnswerRequest;
import com.spotit.api.quiz.dto.AnswerResponse;
import com.spotit.api.quiz.dto.HintRequest;
import com.spotit.api.quiz.dto.HintResponse;
import com.spotit.api.quiz.dto.QuizHomeResponse;
import com.spotit.api.quiz.dto.QuizStats;
import com.spotit.api.quiz.dto.QuizTopicDetail;
import com.spotit.api.quiz.dto.QuizTopicsResponse;
import com.spotit.api.quiz.dto.RoundView;
import com.spotit.api.quiz.dto.StartQuizRequest;
import com.spotit.api.quiz.dto.StartQuizResponse;
import com.spotit.api.quiz.service.QuizPlayService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.spotit.api.support.ControllerTestSupport.USER_ID;
import static com.spotit.api.support.ControllerTestSupport.authenticate;
import static com.spotit.api.support.ControllerTestSupport.clearAuthentication;
import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {
    @Mock QuizPlayService quizPlayService;

    MockMvc mvc;
    final UUID attemptId = UUID.fromString("11111111-2222-3333-4444-555555555555");

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new QuizController(quizPlayService));
        authenticate();
    }

    @AfterEach
    void tearDown() {
        clearAuthentication();
    }

    @Test
    void homeTopicsAndLevelMap() throws Exception {
        when(quizPlayService.home(USER_ID)).thenReturn(new QuizHomeResponse(240, new QuizStats(3, 5, 2), null, List.of(), 31, 5, 3, 5));
        when(quizPlayService.topics(USER_ID)).thenReturn(new QuizTopicsResponse(true, 31, List.of()));
        when(quizPlayService.topic(USER_ID, "my-cycle")).thenReturn(new QuizTopicDetail(null, 240, List.of()));

        mvc.perform(get("/api/v1/quiz/home")).andExpect(status().isOk()).andExpect(jsonPath("$.data.balance").value(240))
                .andExpect(jsonPath("$.data.stats.streak").value(3));
        mvc.perform(get("/api/v1/quiz/topics")).andExpect(status().isOk()).andExpect(jsonPath("$.data.underAge").value(true));
        mvc.perform(get("/api/v1/quiz/topics/my-cycle")).andExpect(status().isOk()).andExpect(jsonPath("$.data.balance").value(240));
    }

    @Test
    void startHintAndAnswer() throws Exception {
        when(quizPlayService.start(USER_ID, new StartQuizRequest("my-cycle", 1))).thenReturn(new StartQuizResponse(attemptId, "my-cycle",
                "My Cycle", 1, "Your period basics", List.of(new RoundView(0, "Stress can delay a period.")), 3, 5, 0, 240));
        when(quizPlayService.hint(USER_ID, attemptId, new HintRequest(0))).thenReturn(new HintResponse(0, "Think about hormones.", 235));
        when(quizPlayService.answer(USER_ID, attemptId, new AnswerRequest(0, "fact"))).thenReturn(new AnswerResponse(0, true, "fact",
                "Because", "Fun", 3, 1, 1, 1, "playing", 0, null));

        mvc.perform(post("/api/v1/quiz/attempts").contentType(MediaType.APPLICATION_JSON).content(json(new StartQuizRequest("my-cycle", 1))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.rounds[0].statement").value("Stress can delay a period."))
                .andExpect(jsonPath("$.data.rounds[0].answer").doesNotExist());
        mvc.perform(post("/api/v1/quiz/attempts/" + attemptId + "/hint").contentType(MediaType.APPLICATION_JSON).content(json(new HintRequest(0))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.balance").value(235));
        mvc.perform(post("/api/v1/quiz/attempts/" + attemptId + "/answers").contentType(MediaType.APPLICATION_JSON)
                        .content(json(new AnswerRequest(0, "fact"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.right").value(true));
    }

    @Test
    void aChoiceMustBeMythOrFact() throws Exception {
        mvc.perform(post("/api/v1/quiz/attempts/" + attemptId + "/answers").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"round\":0,\"choice\":\"maybe\"}"))
                .andExpect(status().is4xxClientError());
        verifyNoInteractions(quizPlayService);
    }
}
