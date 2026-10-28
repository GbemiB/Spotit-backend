package com.spotit.api.quiz.controller;

import com.spotit.api.quiz.dto.ApproveQuestionRequest;
import com.spotit.api.quiz.dto.QuizGroupAdmin;
import com.spotit.api.quiz.dto.QuizLevelAdmin;
import com.spotit.api.quiz.dto.QuizQuestionAdmin;
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
        QuizTopicAdmin topic = new QuizTopicAdmin("my-cycle", "My Cycle", "body", 13, 1, true);
        QuizLevelAdmin level = new QuizLevelAdmin("my-cycle", 1, "Your period basics", 0, true);
        QuizQuestionAdmin question = new QuizQuestionAdmin("q1", "my-cycle", 1, "Stress can delay a period.", "fact", null, "Because", null,
                null, "draft", null, true, null);
        when(quizAdminService.groups()).thenReturn(List.of(group));
        when(quizAdminService.topics()).thenReturn(List.of(topic));
        when(quizAdminService.levels("my-cycle")).thenReturn(List.of(level));
        when(quizAdminService.questions("my-cycle", "draft")).thenReturn(List.of(question));
        when(quizAdminService.saveGroup(eq("body"), any())).thenReturn(group);
        when(quizAdminService.saveTopic(eq("my-cycle"), any())).thenReturn(topic);
        when(quizAdminService.saveLevel(eq("my-cycle"), eq(1), any())).thenReturn(level);
        when(quizAdminService.saveQuestion(eq("q1"), any())).thenReturn(question);
        when(quizAdminService.approve("q1", "Dr Ada")).thenReturn(question);

        mvc.perform(get("/api/v1/config/quiz/groups")).andExpect(jsonPath("$.data[0].id").value("body"));
        mvc.perform(get("/api/v1/config/quiz/topics")).andExpect(jsonPath("$.data[0].minAge").value(13));
        mvc.perform(get("/api/v1/config/quiz/levels").param("topicId", "my-cycle")).andExpect(jsonPath("$.data[0].cost").value(0));
        mvc.perform(get("/api/v1/config/quiz/questions").param("topicId", "my-cycle").param("status", "draft"))
                .andExpect(jsonPath("$.data[0].status").value("draft"));
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
