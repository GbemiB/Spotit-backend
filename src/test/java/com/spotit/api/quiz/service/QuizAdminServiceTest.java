package com.spotit.api.quiz.service;

import com.spotit.api.common.exception.ApiException;
import com.spotit.api.common.exception.ErrorCode;
import com.spotit.api.quiz.dto.QuizGroupAdmin;
import com.spotit.api.quiz.dto.QuizLevelAdmin;
import com.spotit.api.quiz.dto.QuizQuestionAdmin;
import com.spotit.api.quiz.dto.QuizTopicAdmin;
import com.spotit.api.quiz.entity.QuizGroup;
import com.spotit.api.quiz.entity.QuizLevel;
import com.spotit.api.quiz.entity.QuizQuestion;
import com.spotit.api.quiz.entity.QuizTopic;
import com.spotit.api.quiz.repository.QuizGroupRepository;
import com.spotit.api.quiz.repository.QuizLevelRepository;
import com.spotit.api.quiz.repository.QuizQuestionRepository;
import com.spotit.api.quiz.repository.QuizTopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuizAdminServiceTest {
    @Mock QuizGroupRepository groupRepository;
    @Mock QuizTopicRepository topicRepository;
    @Mock QuizLevelRepository levelRepository;
    @Mock QuizQuestionRepository questionRepository;
    @InjectMocks QuizAdminService service;

    @BeforeEach
    void setUp() {
        when(groupRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(topicRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(levelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(questionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(groupRepository.existsById("body")).thenReturn(true);
        when(topicRepository.existsById("my-cycle")).thenReturn(true);
    }

    static QuizQuestionAdmin body(String statement, String answer) {
        return new QuizQuestionAdmin(null, "my-cycle", 1, statement, answer, "Hint", "Explain", "Fun", "NHS", null, null, null, null);
    }

    @Test
    void listsComeFromTheRepositories() {
        when(groupRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(QuizGroup.builder().id("body").label("Body").tone("pink").build()));
        when(topicRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(QuizTopic.builder().id("my-cycle").name("My Cycle").build()));
        when(levelRepository.findAllByOrderByTopicIdAscNumberAsc()).thenReturn(List.of(QuizLevel.builder().topicId("my-cycle").number(1).build()));
        when(levelRepository.findByTopicIdOrderByNumberAsc("my-cycle")).thenReturn(List.of());
        QuizQuestion q = QuizQuestion.builder().id("q1").status("draft").build();
        when(questionRepository.findAllByOrderByTopicIdAscLevelNumberAscIdAsc()).thenReturn(List.of(q));
        when(questionRepository.findByTopicIdOrderByLevelNumberAscIdAsc("my-cycle")).thenReturn(List.of(q));
        when(questionRepository.findByStatusOrderByTopicIdAscLevelNumberAscIdAsc("draft")).thenReturn(List.of(q));
        when(questionRepository.findByTopicIdAndStatusOrderByLevelNumberAscIdAsc("my-cycle", "draft")).thenReturn(List.of());

        assertThat(service.groups()).hasSize(1);
        assertThat(service.topics()).hasSize(1);
        assertThat(service.levels(null)).hasSize(1);
        assertThat(service.levels("my-cycle")).isEmpty();
        assertThat(service.questions(null, null)).hasSize(1);
        assertThat(service.questions("my-cycle", null)).hasSize(1);
        assertThat(service.questions(null, "draft")).hasSize(1);
        assertThat(service.questions("my-cycle", "draft")).isEmpty();
    }

    @Test
    void groupsAreCreatedThenPatched() {
        QuizGroupAdmin created = service.saveGroup("body", new QuizGroupAdmin(null, " Body basics ", "pink", 1, null));
        assertThat(created.label()).isEqualTo("Body basics");
        assertThat(created.active()).isTrue();

        QuizGroup existing = QuizGroup.builder().id("body").label("Body").tone("pink").sortOrder(1).active(true).build();
        when(groupRepository.findById("body")).thenReturn(Optional.of(existing));
        QuizGroupAdmin patched = service.saveGroup("body", new QuizGroupAdmin(null, null, "gold", 4, false));
        assertThat(patched.tone()).isEqualTo("gold");
        assertThat(patched.label()).isEqualTo("Body");
        assertThat(patched.active()).isFalse();
    }

    @Test
    void aNewGroupNeedsItsFields() {
        assertThatThrownBy(() -> service.saveGroup("x", new QuizGroupAdmin(null, null, null, null, null))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void topicsNeedAKnownGroup() {
        QuizTopicAdmin created = service.saveTopic("my-cycle", new QuizTopicAdmin(null, "My Cycle", "body", 18, 2, true));
        assertThat(created.minAge()).isEqualTo(18);
        assertThat(created.sortOrder()).isEqualTo(2);

        assertThatThrownBy(() -> service.saveTopic("t", new QuizTopicAdmin(null, "T", "nope", null, null, null)))
                .isInstanceOf(ApiException.class).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void levelsBelongToATopic() {
        QuizLevelAdmin created = service.saveLevel("my-cycle", 4, new QuizLevelAdmin(null, null, "When you ovulate", 30, null, null));
        assertThat(created.cost()).isEqualTo(30);
        assertThat(created.number()).isEqualTo(4);

        QuizLevel existing = QuizLevel.builder().topicId("my-cycle").number(4).name("Old").cost(30).active(true).build();
        when(levelRepository.findByTopicIdAndNumber("my-cycle", 4)).thenReturn(Optional.of(existing));
        QuizLevelAdmin patched = service.saveLevel("my-cycle", 4, new QuizLevelAdmin(null, null, null, -5, false, 2));
        assertThat(patched.cost()).isZero();
        assertThat(patched.active()).isFalse();
        assertThat(patched.lives()).isEqualTo(2);
        assertThat(service.saveLevel("my-cycle", 4, new QuizLevelAdmin(null, null, null, null, null, 0)).lives()).isNull();
        assertThat(patched.lives()).isEqualTo(2);
        assertThat(service.saveLevel("my-cycle", 4, new QuizLevelAdmin(null, null, null, null, null, 0)).lives()).isNull();

        assertThatThrownBy(() -> service.saveLevel("nope", 1, new QuizLevelAdmin(null, null, "x", 0, null, null)))
                .isInstanceOf(ApiException.class).extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void newQuestionsAreDraftsUntilApproved() {
        QuizQuestionAdmin created = service.saveQuestion("q1", body("Stress can delay a period.", "fact"));
        assertThat(created.status()).isEqualTo(QuizQuestion.DRAFT);

        QuizQuestion stored = QuizQuestion.builder().id("q1").topicId("my-cycle").levelNumber(1).statement("Stress can delay a period.")
                .answer("fact").hint("Hint").explanation("Explain").funFact("Fun").source("NHS").status(QuizQuestion.DRAFT).active(true).build();
        when(questionRepository.findById("q1")).thenReturn(Optional.of(stored));
        QuizQuestionAdmin approved = service.approve("q1", " Dr Ada ");
        assertThat(approved.status()).isEqualTo(QuizQuestion.APPROVED);
        assertThat(approved.reviewedBy()).isEqualTo("Dr Ada");
    }

    @Test
    void changingTheWordingSendsAQuestionBackToDraftButSwitchingOffDoesNot() {
        QuizQuestion stored = QuizQuestion.builder().id("q1").topicId("my-cycle").levelNumber(1).statement("Old").answer("fact")
                .explanation("Explain").status(QuizQuestion.APPROVED).reviewedBy("Dr Ada").active(true).build();
        when(questionRepository.findById("q1")).thenReturn(Optional.of(stored));

        QuizQuestionAdmin off = service.saveQuestion("q1", new QuizQuestionAdmin(null, null, null, null, null, null, null, null, null, null,
                null, false, null));
        assertThat(off.status()).isEqualTo(QuizQuestion.APPROVED);
        assertThat(off.active()).isFalse();

        QuizQuestionAdmin edited = service.saveQuestion("q1", new QuizQuestionAdmin(null, null, 2, "New", "myth", null, null, null, null, null,
                null, null, null));
        assertThat(edited.status()).isEqualTo(QuizQuestion.DRAFT);
        assertThat(edited.reviewedBy()).isNull();
        assertThat(edited.levelNumber()).isEqualTo(2);
    }

    @Test
    void questionsNeedAValidAnswerAndEveryRequiredField() {
        assertThatThrownBy(() -> service.saveQuestion("q9", body("x", "maybe"))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
        assertThatThrownBy(() -> service.saveQuestion("q9", body(null, "myth"))).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void approvingAnUnknownQuestionIs404() {
        assertThatThrownBy(() -> service.approve("nope", "Dr Ada")).isInstanceOf(ApiException.class)
                .extracting("errorCode").isEqualTo(ErrorCode.NOT_FOUND);
    }
}
