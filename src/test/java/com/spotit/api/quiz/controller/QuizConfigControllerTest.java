package com.spotit.api.quiz.controller;

import com.spotit.api.quiz.dto.ApproveQuestionRequest;
import com.spotit.api.quiz.dto.QuizAdminPage;
import com.spotit.api.quiz.dto.QuizChallengeAdmin;
import com.spotit.api.quiz.dto.QuizGroupAdmin;
import com.spotit.api.quiz.dto.QuizLevelAdmin;
import com.spotit.api.quiz.dto.QuizQuestionAdmin;
import com.spotit.api.quiz.dto.QuizRankAdmin;
import com.spotit.api.quiz.dto.QuizTopicAdmin;
import com.spotit.api.quiz.service.QuizAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.spotit.api.support.ControllerTestSupport.json;
import static com.spotit.api.support.ControllerTestSupport.mockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class QuizConfigControllerTest {
    @Mock QuizAdminService quizAdminService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = mockMvc(new QuizConfigController(quizAdminService));
    }

    @Test
    void listsAndSavesEveryKind() throws Exception {
        QuizGroupAdmin group = new QuizGroupAdmin("body", "Body basics", "pink", 1, true);
        QuizTopicAdmin topic = new QuizTopicAdmin("my-cycle", "My Cycle", "body", 13, 1, true, "cycle");
        QuizLevelAdmin level = new QuizLevelAdmin("my-cycle", 1, "Your period basics", 0, true, null);
        QuizQuestionAdmin question = new QuizQuestionAdmin("q1", "my-cycle", 1, "Stress can delay a period.", "fact", null, "Because", null,
                null, "draft", null, true, null);
        QuizRankAdmin rank = new QuizRankAdmin("explorer", "Explorer", 15, true);
        QuizChallengeAdmin challenge = new QuizChallengeAdmin("pad-factory", "game", "Pad Factory", "Design pads", "pad", "pink", 10, true, 13,
                "soon", 2, true);
        when(quizAdminService.challenges(null, null)).thenReturn(new QuizAdminPage<>(List.of(challenge), 0, 50, 1));
        when(quizAdminService.saveChallenge(eq("pad-factory"), any())).thenReturn(challenge);
        when(quizAdminService.ranks(null, null)).thenReturn(new QuizAdminPage<>(List.of(rank), 0, 50, 1));
        when(quizAdminService.saveRank(eq("explorer"), any())).thenReturn(rank);
        when(quizAdminService.groups(null, null)).thenReturn(new QuizAdminPage<>(List.of(group), 0, 50, 1));
        when(quizAdminService.topics(null, null)).thenReturn(new QuizAdminPage<>(List.of(topic), 0, 50, 1));
        when(quizAdminService.levels("my-cycle", null, null)).thenReturn(new QuizAdminPage<>(List.of(level), 0, 50, 1));
        when(quizAdminService.questions("my-cycle", "draft", 2, 10)).thenReturn(new QuizAdminPage<>(List.of(question), 2, 10, 21));
        when(quizAdminService.saveGroup(eq("body"), any())).thenReturn(group);
        when(quizAdminService.saveTopic(eq("my-cycle"), any())).thenReturn(topic);
        when(quizAdminService.saveLevel(eq("my-cycle"), eq(1), any())).thenReturn(level);
        when(quizAdminService.saveQuestion(eq("q1"), any())).thenReturn(question);
        when(quizAdminService.approve("q1", "Dr Ada")).thenReturn(question);

        mvc.perform(get("/api/v1/config/quiz/challenges")).andExpect(jsonPath("$.data.items[0].cost").value(10));
        mvc.perform(put("/api/v1/config/quiz/challenges/pad-factory").contentType(MediaType.APPLICATION_JSON).content(json(challenge)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/quiz/ranks")).andExpect(jsonPath("$.data.items[0].minStars").value(15));
        mvc.perform(put("/api/v1/config/quiz/ranks/explorer").contentType(MediaType.APPLICATION_JSON).content(json(rank))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/config/quiz/groups")).andExpect(jsonPath("$.data.items[0].id").value("body"));
        mvc.perform(get("/api/v1/config/quiz/topics")).andExpect(jsonPath("$.data.items[0].minAge").value(13));
        mvc.perform(get("/api/v1/config/quiz/levels").param("topicId", "my-cycle")).andExpect(jsonPath("$.data.items[0].cost").value(0));
        mvc.perform(get("/api/v1/config/quiz/questions").param("topicId", "my-cycle").param("status", "draft").param("page", "2").param("size", "10"))
                .andExpect(jsonPath("$.data.items[0].status").value("draft")).andExpect(jsonPath("$.data.total").value(21));
        mvc.perform(put("/api/v1/config/quiz/groups/body").contentType(MediaType.APPLICATION_JSON).content(json(group))).andExpect(status().isOk());
        mvc.perform(put("/api/v1/config/quiz/topics/my-cycle").contentType(MediaType.APPLICATION_JSON).content(json(topic))).andExpect(status().isOk());
        mvc.perform(put("/api/v1/config/quiz/levels/my-cycle/1").contentType(MediaType.APPLICATION_JSON).content(json(level)))
                .andExpect(status().isOk());
        mvc.perform(put("/api/v1/config/quiz/questions/q1").contentType(MediaType.APPLICATION_JSON).content(json(question)))
                .andExpect(status().isOk());
        mvc.perform(post("/api/v1/config/quiz/questions/q1/approve").contentType(MediaType.APPLICATION_JSON)
                .content(json(new ApproveQuestionRequest("Dr Ada")))).andExpect(status().isOk());
    }
}
